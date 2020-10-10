import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Servidor implements InterfazServidorServidor, InterfazServidorCliente {

	private String nombreServidor;
	private String ipOtroServidor;
	private ArrayList<String> usuarios;
	private ArrayList<Integer> donacionUsuarios;
	private int subtotalDonado;

	public Servidor(String nombreServidor, String ipOtroServidor) {
		this.nombreServidor = nombreServidor;
		this.ipOtroServidor = ipOtroServidor;
		this.usuarios = new ArrayList<String>();
		this.donacionUsuarios = new ArrayList<Integer>();
	}

	public String obtenerNombreServidorReplica(){
		String nombreOtroServidor;

		if (nombreServidor.equals("servidorReplica1")){
			nombreOtroServidor = "servidorReplica2";
		} else {
			nombreOtroServidor = "servidorReplica1";
		}

		return (nombreOtroServidor);
	}

	public InterfazServidorServidor obtenerObjetoRemotoServidor(){
		InterfazServidorServidor instanciaLocal = null;

		try {
			Registry registry = LocateRegistry.getRegistry(ipOtroServidor);
			instanciaLocal = (InterfazServidorServidor) registry.lookup(obtenerNombreServidorReplica());
		} catch (Exception e) {
			System.err.println("exception:");
			e.printStackTrace();
		}

		return (instanciaLocal);
	}

	public String registrar(String usuario) throws RemoteException {
		InterfazServidorServidor instanciaLocal = obtenerObjetoRemotoServidor();
		String resultado;

		if (!comprobarSiRegistrado(usuario) && !instanciaLocal.comprobarSiRegistrado(usuario)){
			if (numUsuariosRegistrados() > instanciaLocal.numUsuariosRegistrados()){
				instanciaLocal.registrarServidorServidor(usuario);
			} else {
				registrarServidorServidor(usuario);
			}
			resultado = "Se ha registrado con exito";
		} else {
			resultado = "Ya estas registrado";
		}

		return (resultado);
	}

	public void registrarServidorServidor(String usuario){
		usuarios.add(usuario);
		donacionUsuarios.add(0);
		System.out.println("Se ha registrado al usuario " + usuarios.get(usuarios.size()-1)+ " en este servidor");
	}

	public boolean comprobarSiRegistrado(String usuario){
		return (usuarios.contains(usuario));
	}
	
	public int numUsuariosRegistrados(){
		return (usuarios.size());
	}

	public String donar(int cantidad, String usuario) throws RemoteException {
		String resultado;		
	
		if (comprobarSiRegistrado(usuario)){
			resultado = donarServidorServidor(cantidad, usuario);
		} else {
			InterfazServidorServidor instanciaLocal = obtenerObjetoRemotoServidor();

			if (instanciaLocal.comprobarSiRegistrado(usuario)){
				resultado = instanciaLocal.donarServidorServidor(cantidad,usuario);
			} else {
				resultado = "Usuario no esta registrado";
			}
		}

		return (resultado);
	}

	public String donarServidorServidor(int cantidad, String usuario){
		int indiceUsuario = usuarios.indexOf(usuario);
		int cantidadDonada = donacionUsuarios.get(indiceUsuario) + cantidad;
		subtotalDonado = subtotalDonado + cantidad;
		donacionUsuarios.set(indiceUsuario, cantidadDonada);

		System.out.println("El usuario " + usuario + " ha donado " + cantidad);

		return ("Donacion realizada");
	}

	public String verTotalDonado(String usuario) throws RemoteException {
		InterfazServidorServidor instanciaLocal = obtenerObjetoRemotoServidor();
		String resultado;
	
		if (comprobarSiRegistrado(usuario) || instanciaLocal.comprobarSiRegistrado(usuario)){
			if (haDonado(usuario) || instanciaLocal.haDonado(usuario)){
				int totalDonado = verSubtotalDonado() + instanciaLocal.verSubtotalDonado();
				resultado = "Total donado: "+ totalDonado;
			} else {
				resultado = "Debes haber donado para ver el total donado";
			}
		} else {
			resultado = "Usuario no registrado";
		}

		return (resultado);
	}

	public boolean haDonado(String usuario){
		boolean haDonado;		

		if (comprobarSiRegistrado(usuario)){
			if (donacionUsuarios.get(usuarios.indexOf(usuario)) == 0){
				haDonado = false;
			} else {
				haDonado = true;
			}
		} else {
			haDonado = false;
		}

		return (haDonado);
	}

	public int verSubtotalDonado(){
		System.out.println("El subtotal donado de este servidor es: " + subtotalDonado);

		return (subtotalDonado);
	}

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		if (args.length < 2){
			System.out.println("Uso: <idServidor> <ipDelOtroServidor>");
			System.exit(1);
		}

		Registry registry = null;

		try {
			registry = LocateRegistry.createRegistry(1099);
			System.out.println("Registry creado");
		} catch (Exception e) {
			try {
				registry = LocateRegistry.getRegistry();
			} catch (Exception exception){
				System.out.println("No se ha podido obtener el registry: ");
				exception.printStackTrace();
				System.exit(1);
			}
			
			System.out.println("Registry obtenido");
		}

		try {
			String nombre_objeto_remoto_servidor = "servidorReplica"+args[0];

			Servidor unServidor = new Servidor(nombre_objeto_remoto_servidor,args[1]);
			InterfazServidorServidor stubServidor = (InterfazServidorServidor) UnicastRemoteObject.exportObject(unServidor, 0);

			registry.rebind(nombre_objeto_remoto_servidor, stubServidor);

			System.out.println("Servidor " + nombre_objeto_remoto_servidor + " preparado");
		} catch (Exception e) {
			System.err.println("exception:");
			e.printStackTrace();
		}
	}

}
