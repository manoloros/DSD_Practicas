/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include "dns.h"
#include <stdio.h>
#include <stdlib.h>
#include <rpc/pmap_clnt.h>
#include <string.h>
#include <memory.h>
#include <sys/socket.h>
#include <netinet/in.h>

#ifndef SIG_PF
#define SIG_PF void(*)(int)
#endif

static char **
_comprobar_1 (char * *argp, struct svc_req *rqstp)
{
	return (comprobar_1_svc(*argp, rqstp));
}

static int *
_anadirdominio_1 (anadirdominio_1_argument *argp, struct svc_req *rqstp)
{
	return (anadirdominio_1_svc(argp->arg1, argp->arg2, rqstp));
}

static int *
_borrardominio_1 (char * *argp, struct svc_req *rqstp)
{
	return (borrardominio_1_svc(*argp, rqstp));
}

static void
dns_1(struct svc_req *rqstp, register SVCXPRT *transp)
{
	union {
		char *comprobar_1_arg;
		anadirdominio_1_argument anadirdominio_1_arg;
		char *borrardominio_1_arg;
	} argument;
	char *result;
	xdrproc_t _xdr_argument, _xdr_result;
	char *(*local)(char *, struct svc_req *);

	switch (rqstp->rq_proc) {
	case NULLPROC:
		(void) svc_sendreply (transp, (xdrproc_t) xdr_void, (char *)NULL);
		return;

	case COMPROBAR:
		_xdr_argument = (xdrproc_t) xdr_wrapstring;
		_xdr_result = (xdrproc_t) xdr_wrapstring;
		local = (char *(*)(char *, struct svc_req *)) _comprobar_1;
		break;

	case ANADIRDOMINIO:
		_xdr_argument = (xdrproc_t) xdr_anadirdominio_1_argument;
		_xdr_result = (xdrproc_t) xdr_int;
		local = (char *(*)(char *, struct svc_req *)) _anadirdominio_1;
		break;

	case BORRARDOMINIO:
		_xdr_argument = (xdrproc_t) xdr_wrapstring;
		_xdr_result = (xdrproc_t) xdr_int;
		local = (char *(*)(char *, struct svc_req *)) _borrardominio_1;
		break;

	default:
		svcerr_noproc (transp);
		return;
	}
	memset ((char *)&argument, 0, sizeof (argument));
	if (!svc_getargs (transp, (xdrproc_t) _xdr_argument, (caddr_t) &argument)) {
		svcerr_decode (transp);
		return;
	}
	result = (*local)((char *)&argument, rqstp);
	if (result != NULL && !svc_sendreply(transp, (xdrproc_t) _xdr_result, result)) {
		svcerr_systemerr (transp);
	}
	if (!svc_freeargs (transp, (xdrproc_t) _xdr_argument, (caddr_t) &argument)) {
		fprintf (stderr, "%s", "unable to free arguments");
		exit (1);
	}
	return;
}

int
main (int argc, char **argv)
{
	register SVCXPRT *transp;

	pmap_unset (DNS, PRIMERA);

	transp = svcudp_create(RPC_ANYSOCK);
	if (transp == NULL) {
		fprintf (stderr, "%s", "cannot create udp service.");
		exit(1);
	}
	if (!svc_register(transp, DNS, PRIMERA, dns_1, IPPROTO_UDP)) {
		fprintf (stderr, "%s", "unable to register (DNS, PRIMERA, udp).");
		exit(1);
	}

	transp = svctcp_create(RPC_ANYSOCK, 0, 0);
	if (transp == NULL) {
		fprintf (stderr, "%s", "cannot create tcp service.");
		exit(1);
	}
	if (!svc_register(transp, DNS, PRIMERA, dns_1, IPPROTO_TCP)) {
		fprintf (stderr, "%s", "unable to register (DNS, PRIMERA, tcp).");
		exit(1);
	}

	svc_run ();
	fprintf (stderr, "%s", "svc_run returned");
	exit (1);
	/* NOTREACHED */
}
