package ut4.pruebas;

import java.util.List;
import ut4.controlador.HangarControl;
import ut4.modelo.Nave;
import ut4.util.HibernateUtil;

/**
 * Pruebas del Sistema de Control de Hangares "Death Star".
 */
public class MainHangar {

    public static void main(String[] args) {
        System.out.println("======= SISTEMA DE CONTROL DE HANGARES \"DEATH STAR\" =======");

        HangarControl control = new HangarControl();

        try {
            
            System.out.println("\n1. Registrando nuevo caza en el hangar...");

            // Estado transitorio
            Nave interceptor = new Nave("TIE Interceptor", "TI-v2", 1, "Critico");
            control.registrarNuevaNave(interceptor);
            System.out.println(interceptor);

            
            System.out.println("\n2. Reparando el caza con ID " + interceptor.getId() + "...");
            control.actualizarEstadoNave(interceptor.getId(), "Estable");

            
            String modeloBuscado = "TI-v2";
            System.out.println("\n3. Escaneando cazas del modelo [" + modeloBuscado + "]...");

            List<Nave> cazas = control.obtenerCazasPorModelo(modeloBuscado);

            System.out.println("--- Reporte de Cazas " + modeloBuscado + " ---");
            if (!cazas.isEmpty()) {
                for (Nave caza : cazas) {
                    System.out.println("ID: " + caza.getId()
                                     + " | Modelo: " + caza.getModelo()
                                     + " | Nombre: " + caza.getNombre()
                                     + " | Estado: " + caza.getEstadoSistema());
                }
            } else {
                System.out.println("No hay cazas de este modelo en el hangar.");
            }

        } finally {
            HibernateUtil.shutdown();
            System.out.println("======================================================");
        }
    }
}
