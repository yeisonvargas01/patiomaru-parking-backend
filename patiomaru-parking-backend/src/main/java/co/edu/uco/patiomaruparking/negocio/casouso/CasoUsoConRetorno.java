package co.edu.uco.patiomaruparking.negocio.casouso;

public interface CasoUsoConRetorno<D, R> {

	R ejecutar(D datos);
}
