/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "dns2.h"


void
dns2_1(char *host, char *modo, char*buscar, char*ip)
{
	CLIENT *clnt;
	char * *result_1;
	char *comprobar_1_arg1;
	int  *result_2;
	char *anadirdominio_1_arg1;
	char *anadirdominio_1_arg2;
	int  *result_3;
	char *borrardominio_1_arg1;

#ifndef	DEBUG
	clnt = clnt_create (host, DNS2, PRIMERA, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}
#endif	/* DEBUG */  

	if (!strcmp(modo, "comprobar")){
		result_1 = comprobar_1(buscar, clnt);
		if (result_1 == (char **) NULL) {
			clnt_perror (clnt, "call failed");
		} else {
			printf("Resultado de busqueda de IP: %s\n",*result_1);
		}
	} else {
		if (!strcmp(modo, "anadir")){	
			result_2 = anadirdominio_1(buscar,ip, clnt);
		} else if (!strcmp(modo, "borrar")) {
			result_2 = borrardominio_1(buscar, clnt);
		}

		if (result_2 == (int *) NULL) {
			clnt_perror (clnt, "call failed");
		} else {
			printf("Resultado de %s (0 ha funcionado, 1 ha ocurrido un error): %i\n",modo,*result_2);
		}
	}

#ifndef	DEBUG
	clnt_destroy (clnt);
#endif	 /* DEBUG */
}


int
main (int argc, char *argv[])
{
	char * host;
	char * modo;
	char * buscar;
	char *ip;

	if (argc < 4) {
		printf ("usage: <server_host> <modo> <nombre> <IP>\n");
		exit (1);
	}

	host = argv[1];
	modo = argv[2];
	buscar = argv[3];
	ip = "";

	if (!strcmp(modo, "anadir")){
		if (argc < 5){
			printf ("usage: <server_host> <modo> <nombre> <IP>\n");
			exit(1);
		}
		ip = argv[4];
	}
	
	dns2_1 (host,modo,buscar,ip);
exit (0);
}
