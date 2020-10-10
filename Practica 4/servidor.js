var http = require("http");
var url = require("url");
var fs = require("fs");
var path = require("path");
var socketio = require("socket.io");
var MongoClient = require('mongodb').MongoClient;
var MongoServer = require('mongodb').Server;
var mimeTypes = { "html": "text/html", "jpeg": "image/jpeg", "jpg": "image/jpeg", "png": "image/png", "js": "text/javascript", "css": "text/css", "swf": "application/x-shockwave-flash"};


var httpServer = http.createServer(
	function(request, response) {
		var uri = url.parse(request.url).pathname;
		if (uri=="/") uri = "/usuario.html";
		var fname = path.join(process.cwd(), uri);
		fs.exists(fname, function(exists) {
			if (exists) {
				fs.readFile(fname, function(err, data){
					if (!err) {
						var extension = path.extname(fname).split(".")[1];
						var mimeType = mimeTypes[extension];
						response.writeHead(200, mimeType);
						response.write(data);
						response.end();
					}
					else {
						response.writeHead(200, {"Content-Type": "text/plain"});
						response.write('Error de lectura en el fichero: '+uri);
						response.end();
					}
				});
			}
			else{
				console.log("Peticion invalida: "+uri);
				response.writeHead(200, {"Content-Type": "text/plain"});
				response.write('404 Not Found\n');
				response.end();
			}
		});
	}
);

var persianaCerrada = false;
var AC = false;
var valoresObtenidos = false;
var temperatura;
var luminosidad;
var maximoTemp;
var minimoTemp;
var maximoLum;
var minimoLum;

MongoClient.connect("mongodb://localhost:27017/", function(err, db) {
	httpServer.listen(8080);
	var io = socketio.listen(httpServer);
	var dbo = db.db("midb");
	
	dbo.createCollection("historicoMedidas", function(err, collection){
	});
			
	function cambiarEstado(tipo){
		if (tipo === "persiana")
			persianaCerrada = !persianaCerrada;
		else
			AC = !AC;
	}	

	function comprobarValores(){
		var alarma1 = "";
		var alarma2 = "";
		var alarma3 = "";
		var alarma4 = "";
		var alarma5 = "";

		if (temperatura > maximoTemp)
			alarma1 = "La temperatura ha superado el valor maximo permitido";
		

		if (temperatura < minimoTemp)
			alarma2 = "La temperatura tiene un valor mas pequeño que el valor minimo permitido";
		

		if (luminosidad > maximoLum)
			alarma3 = "La luminosidad ha superado el valor maximo permitido";

		if (luminosidad < minimoLum)
			alarma4 = "La luminosidad tiene un valor mas pequeño que el valor minimo permitido";

		if (temperatura > maximoTemp && luminosidad > maximoLum && !persianaCerrada){
			persianaCerrada = true;
			alarma5 = "Se ha cerrado la persiana automaticamente";
			io.sockets.emit('actualizarActuadores', {persianaCerrada, AC});
		}

		io.sockets.emit('actualizarUsuarioAlarmas', {alarma1, alarma2, alarma3, alarma4, alarma5});
	}

	io.sockets.on('connection', function(client) {
		client.on('actualizarServidor', function(data) {
			console.log("Nuevos valores de los sensores recibidos");
			luminosidad = parseInt(data.luminosidad);
			temperatura = parseInt(data.temperatura);
			maximoTemp = parseInt(data.maximoTemp);
			minimoTemp = parseInt(data.minimoTemp);
			maximoLum = parseInt(data.maximoLum);
			minimoLum = parseInt(data.minimoLum);
			valoresObtenidos = true;

			var fecha = new Date();
			dbo.collection("historicoMedidas").insertOne(({luminosidad:luminosidad, temperatura:temperatura, fecha:fecha}), {safe:true}, function(err, result) {});

			io.sockets.emit('actualizarUsuarioSensores', {luminosidad, temperatura});
			comprobarValores();
		});

		client.on('cambiarEstado', function(tipo) {
			cambiarEstado(tipo);	
			io.sockets.emit('actualizarActuadores', {persianaCerrada, AC});
		});

		client.on('verContenido', function() {
			dbo.collection("historicoMedidas").find({}).toArray(function(err, result) {
				if (err) throw err;
				client.emit('verContenido',result);
			});
		});

		client.emit('actualizarActuadores', {persianaCerrada, AC});
		if (valoresObtenidos)
			client.emit('actualizarUsuarioSensores', {luminosidad, temperatura});

	});
});

console.log("Servidor iniciado");

