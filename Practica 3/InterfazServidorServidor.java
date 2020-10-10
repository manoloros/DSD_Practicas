import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfazServidorServidor extends Remote {
	public void registrarServidorServidor(String usuario) throws RemoteException;
	public String donarServidorServidor(int cantidad, String usuario) throws RemoteException;
	public int verSubtotalDonado() throws RemoteException;
	public boolean comprobarSiRegistrado(String usuario) throws RemoteException;
	public boolean haDonado(String usuario) throws RemoteException;
	public int numUsuariosRegistrados() throws RemoteException;
}
