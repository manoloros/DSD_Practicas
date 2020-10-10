Funcionamiento:

En este directorio tenemos el cliente y servidor DNS, el servidor DNS incluye código para hacer de cliente y conectarse al servidor DNS2, el cliente y servidor DNS2 están guardados en la carpeta DNS2, utilizamos ese cliente DNS2 para añadirle dominios al servidor DNS2.

Al ejecutar dns_client hay que pasarle los siguientes parámetros:

./dns_client localhost <modo> <nombre> <IP>

Modo puede ser uno de estos tres valores: anadir, si queremos añadir un dominio con una IP determinada al servidor, borrar, en este caso no habrá que pasarle ninguna IP, solo el nombre, y borrará del servidor el dominio con ese nombre y por último comprobar, en este tampoco hará falta añadirle IP, lo que hace es comprobar en el servidor DNS si existe algún dominio con ese nombre, si no lo encuentra, el servidor DNS le preguntará al servidor DNS2, en caso de encontrarlo devuelve la IP de ese dominio, si no lo encuentra devuelve la siguiente frase: "No encontrado".

dns2_client tiene los mismos parámetros que dns_client y se comunica con el servidor DNS2 directamente.



