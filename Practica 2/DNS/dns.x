program DNS {
	version PRIMERA {
		string COMPROBAR(string) = 1;
		int ANADIRDOMINIO(string, string) = 2;
		int BORRARDOMINIO(string) = 3;
	} = 1;
} = 0x20000155;

