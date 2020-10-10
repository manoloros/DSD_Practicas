Cliente:

Parámetros: <host> <nombreDelServidor> <operacion> <nombreUsuario> <cantidad>(Cantidad solo si estamos donando)

<host> IP de alguno de los servidores, en nuestro caso como los tenemos todos (los dos servidores y el cliente) en la misma máquina usamos localhost.

<nombreDelServidor> puede ser uno de estos dos valores: servidorReplica1 o servidorReplica2.

<operacion> puede ser uno de estos tres valores: registrar, para registrarse, donar, para donar, verTotal, para ver la cantidad total que se ha donado entre todos los usuarios de las dos réplicas.

<nombreUsuario> el nombre del usuario que queremos registrar o queremos usar para donar o ver el total donado.

<cantidad> la cantidad que queremos donar, solo lo usamos si queremos donar y debe ser un número entero.

Servidor:

Parámetros: <idServidor> <ipDelOtroServidor>

<idServidor> puede ser uno de estos dos valores: 1 o 2, al servidor que le demos el valor 1 será el servidorReplica1, al que le demos el 2, servidorReplica2.

<ipDelOtroServidor> IP del otro servidor, como tenemos los dos servidores en la misma máquina usamos localhost.

Servidor se encargará de lanzar el ligador de RMI, no es necesario lanzarlo aparte.
