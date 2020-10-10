import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente {

	public static void main(String args[]) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		if (args.length < 4){
			System.out.println("Uso: <host> <nombreDelServidor> <operacion> <nombreUsuario> <cantidad>(Cantidad solo si estamos donando)");
			System.exit(1);
		}

		try {
			Registry registry = LocateRegistry.getRegistry(args[0]);
			InterfazServidorCliente instanciaLocal = (InterfazServidorCliente) registry.lookup(args[1]);

			switch (args[2]){
				case "registrar":
					System.out.println(instanciaLocal.registrar(args[3]));
				break;
				case "donar":
					if (args.length < 5){
						System.out.println("Uso: <host> <nombreDelServidor> <operacion> <nombreUsuario> <cantidad>(Cantidad solo si estamos donando)");
						System.exit(1);
					} else {
						System.out.println(instanciaLocal.donar(Integer.parseInt(args[4]),args[3]));
					}
				break;
				case "verTotal":
					System.out.println(instanciaLocal.verTotalDonado(args[3]));
				break;
				default:
					System.out.println("Debe utilizar uno de estos tres valores: registrar, donar o verTotal");
				break;
			}

		} catch (Exception e) {
			System.err.println("exception:");
			e.printStackTrace();
		}
	}
}
