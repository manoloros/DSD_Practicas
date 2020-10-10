import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfazServidorCliente extends Remote {
	public String registrar(String usuario) throws RemoteException;
	public String donar(int cantidad, String usuario) throws RemoteException;
	public String verTotalDonado(String usuario) throws RemoteException;
}
