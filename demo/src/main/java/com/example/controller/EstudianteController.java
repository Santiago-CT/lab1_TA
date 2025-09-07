package com.example.controller;//package com.example.controller;

import com.example.dao.EstudianteDao;
import com.example.model.Estudiante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

/**
 * Controlador de Lógica de Negocio para la gestión de estudiantes
 * Se encarga de las operaciones de datos y la lógica de negocio
 */
public class EstudianteController {

    /**
     * Obtiene la lista de todos los estudiantes desde la base de datos
     * @return ObservableList de estudiantes para usar en la tabla
     * @throws Exception si hay problemas de acceso a datos
     */
    public ObservableList<Object> obtenerListaEstudiantes() throws Exception {
        try {
            List<Estudiante> estudiantes = EstudianteDao.get();
            // Convertir la lista de Estudiante a lista de Object
            return FXCollections.observableArrayList(estudiantes.toArray());
        } catch (Exception e) {
            throw new Exception("Error al obtener la lista de estudiantes: " + e.getMessage(), e);
        }
    }

    /**
     * Inserta un nuevo estudiante en la base de datos
     * @param estudianteObj El objeto estudiante a insertar
     * @return true si la inserción fue exitosa, false en caso contrario
     * @throws Exception si hay problemas de acceso a datos
     */
    public boolean insertarEstudiante(Object estudianteObj) throws Exception {
        if (!(estudianteObj instanceof Estudiante estudiante)) {
            throw new Exception("El objeto proporcionado no es un estudiante válido");
        }

        try {
            // Validar datos antes de insertar
            if (!validarDatosEstudiante(estudiante)) {
                throw new Exception("Los datos del estudiante no son válidos");
            }

            // Verificar que el código no esté en uso
            if (EstudianteDao.existeCodigoEstudiante(estudiante.getCodigo(), -1)) {
                throw new Exception("El código de estudiante ya está en uso");
            }

            // Verificar que el email no esté en uso
            if (EstudianteDao.existeEmail(estudiante.getEmail(), -1)) {
                throw new Exception("El email ya está en uso por otra persona");
            }

            EstudianteDao.insert(estudiante);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al insertar el estudiante: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza un estudiante existente en la base de datos
     * @param estudianteObj El objeto estudiante a actualizar
     * @return true si la actualización fue exitosa, false en caso contrario
     * @throws Exception si hay problemas de acceso a datos
     */


    /**
     * Elimina un estudiante de la base de datos
     * @param estudianteId ID del estudiante a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @throws Exception si hay problemas de acceso a datos
     */

    public boolean eliminarEstudiante(double estudianteId) throws Exception {
        try {
            // Verificar que el estudiante existe antes de eliminar
            if (!EstudianteDao.existeEstudiante(estudianteId)) {
                throw new Exception("El estudiante no existe en la base de datos");
            }

            return EstudianteDao.eliminar(estudianteId);
        } catch (Exception e) {
            throw new Exception("Error al eliminar el estudiante: " + e.getMessage(), e);
        }
    }

    // Métodos para obtener datos específicos de un objeto estudiante

    /**
     * Obtiene el código de un estudiante
     */
    public double obtenerCodigoEstudiante(Object estudianteObj) {
        if (estudianteObj instanceof Estudiante estudiante) {
            return estudiante.getCodigo();
        }
        return 0.0;
    }

    /**
     * Obtiene el ID de un estudiante
     */
    public double obtenerIdEstudiante(Object estudianteObj) {
        if (estudianteObj instanceof Estudiante estudiante) {
            return estudiante.getID();
        }
        return 0.0;
    }

    /**
     * Obtiene el nombre completo de un estudiante
     */
    public String obtenerNombreCompleto(Object estudianteObj) {
        if (estudianteObj instanceof Estudiante estudiante) {
            return estudiante.getNombres() + " " + estudiante.getApellidos();
        }
        return "";
    }

    /**
     * Obtiene los nombres de un estudiante
     */
    public String obtenerNombres(Object estudianteObj) {
        if (estudianteObj instanceof Estudiante estudiante) {
            return estudiante.getNombres();
        }
        return "";
    }

    /**
     * Obtiene los apellidos de un estudiante
     */
    public String obtenerApellidos(Object estudianteObj) {
        if (estudianteObj instanceof Estudiante estudiante) {
            return estudiante.getApellidos();
        }
        return "";
    }

    /**
     * Obtiene el email de un estudiante
     */
    public String obtenerEmail(Object estudianteObj) {
        if (estudianteObj instanceof Estudiante estudiante) {
            return estudiante.getEmail();
        }
        return "";
    }

    /**
     * Obtiene el nombre del programa de un estudiante
     */
    public String obtenerNombrePrograma(Object estudianteObj) {
        if (estudianteObj instanceof Estudiante estudiante) {
            return estudiante.getPrograma() != null ?
                    estudiante.getPrograma().getNombre() : "Sin programa";
        }
        return "Sin programa";
    }

    /**
     * Obtiene el programa completo de un estudiante
     */
    public Object obtenerPrograma(Object estudianteObj) {
        if (estudianteObj instanceof Estudiante estudiante) {
            return estudiante.getPrograma();
        }
        return null;
    }

    /**
     * Obtiene el estado de un estudiante
     */
    public String obtenerEstado(Object estudianteObj) {
        if (estudianteObj instanceof Estudiante estudiante) {
            return estudiante.isActivo() ? "Activo" : "Inactivo";
        }
        return "Desconocido";
    }

    /**
     * Obtiene el estado booleano de un estudiante
     */
    public boolean obtenerEstadoBooleano(Object estudianteObj) {
        if (estudianteObj instanceof Estudiante estudiante) {
            return estudiante.isActivo();
        }
        return false;
    }

    /**
     * Obtiene el promedio de un estudiante
     */
    public double obtenerPromedio(Object estudianteObj) {
        if (estudianteObj instanceof Estudiante estudiante) {
            return estudiante.getPromedio();
        }
        return 0.0;
    }

    /**
     * Obtiene la fecha de ingreso de un estudiante
     */
    public String obtenerFechaIngreso(Object estudianteObj) {
        // Por ahora retorna N/A, pero aquí podrías implementar la lógica
        // para obtener la fecha de ingreso si está disponible en el modelo
        return "N/A";
    }

    /**
     * Valida si un estudiante puede ser eliminado
     * @param estudianteObj El objeto estudiante a validar
     * @return true si puede ser eliminado, false en caso contrario
     */
    public boolean puedeEliminarEstudiante(Object estudianteObj) {
        if (!(estudianteObj instanceof Estudiante)) {
            return false;
        }

        Estudiante estudiante = (Estudiante) estudianteObj;

        // Verificar si el estudiante existe en la base de datos
        try {
            return EstudianteDao.existeEstudiante(estudiante.getID());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Genera el mensaje de confirmación para eliminar un estudiante
     * @param estudianteObj El objeto estudiante a eliminar
     * @return String con el mensaje formateado
     */
    public String generarMensajeConfirmacionEliminacion(Object estudianteObj) {
        if (!(estudianteObj instanceof Estudiante estudiante)) {
            return "Error: Objeto no válido";
        }

        return String.format("""
            ¿Está seguro que desea eliminar al estudiante?
            
            Nombre: %s %s
            Código: %.0f
            Email: %s
            Programa: %s
            
            ⚠️ ADVERTENCIA: Esta acción también eliminará:
            • Todas las inscripciones del estudiante
            • Toda la información personal asociada
            
            Esta acción NO se puede deshacer.
            """,
                estudiante.getNombres(),
                estudiante.getApellidos(),
                estudiante.getCodigo(),
                estudiante.getEmail(),
                estudiante.getPrograma() != null ? estudiante.getPrograma().getNombre() : "Sin programa"
        );
    }

    /**
     * Genera los detalles completos de un estudiante para mostrar
     * @param estudianteObj El objeto estudiante del cual mostrar detalles
     * @return String con los detalles formateados
     */
    public String generarDetallesEstudiante(Object estudianteObj) {
        if (!(estudianteObj instanceof Estudiante estudiante)) {
            return "Error: Objeto no válido";
        }

        return String.format("""
            Detalles del Estudiante:
            
            ID: %.0f
            Código: %.0f
            Nombres: %s
            Apellidos: %s
            Email: %s
            Programa: %s
            Facultad: %s
            Promedio: %.2f
            Estado: %s
            """,
                estudiante.getID(),
                estudiante.getCodigo(),
                estudiante.getNombres(),
                estudiante.getApellidos(),
                estudiante.getEmail(),
                estudiante.getPrograma() != null ? estudiante.getPrograma().getNombre() : "Sin programa",
                estudiante.getPrograma() != null && estudiante.getPrograma().getFacultad() != null ?
                        estudiante.getPrograma().getFacultad().getNombre() : "Sin facultad",
                estudiante.getPromedio(),
                estudiante.isActivo() ? "Activo" : "Inactivo"
        );
    }

    /**
     * Busca un estudiante por su código
     * @param codigo Código del estudiante a buscar
     * @return El objeto estudiante encontrado o null si no existe
     * @throws Exception si hay problemas de acceso a datos
     */
    public Object buscarEstudiantePorCodigo(double codigo) throws Exception {
        try {
            List<Estudiante> estudiantes = EstudianteDao.get();
            return estudiantes.stream()
                    .filter(e -> e.getCodigo() == codigo)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new Exception("Error al buscar el estudiante: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un estudiante por su ID
     * @param id ID del estudiante a buscar
     * @return El objeto estudiante encontrado o null si no existe
     * @throws Exception si hay problemas de acceso a datos
     */
    public Object buscarEstudiantePorId(double id) throws Exception {
        try {
            List<Estudiante> estudiantes = EstudianteDao.get();
            return estudiantes.stream()
                    .filter(e -> e.getID() == id)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new Exception("Error al buscar el estudiante: " + e.getMessage(), e);
        }
    }

    /**
     * Valida los datos de un estudiante antes de guardarlo
     * @param estudianteObj El objeto estudiante a validar
     * @return true si los datos son válidos, false en caso contrario
     */
    public boolean validarDatosEstudiante(Object estudianteObj) {
        if (!(estudianteObj instanceof Estudiante estudiante)) {
            return false;
        }

        // Validar campos obligatorios
        if (estudiante.getNombres() == null || estudiante.getNombres().trim().isEmpty()) {
            return false;
        }

        if (estudiante.getApellidos() == null || estudiante.getApellidos().trim().isEmpty()) {
            return false;
        }

        if (estudiante.getEmail() == null || estudiante.getEmail().trim().isEmpty()) {
            return false;
        }

        // Validar formato de email
        if (!validarFormatoEmail(estudiante.getEmail())) {
            return false;
        }

        // Validar código positivo
        if (estudiante.getCodigo() <= 0) {
            return false;
        }

        // Validar promedio entre 0 y 5
        if (estudiante.getPromedio() < 0 || estudiante.getPromedio() > 5) {
            return false;
        }

        // Validar que tenga programa asignado
        if (estudiante.getPrograma() == null) {
            return false;
        }

        return true;
    }

    /**
     * Valida el formato del email
     * @param email Email a validar
     * @return true si el formato es válido, false en caso contrario
     */
    private boolean validarFormatoEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Verifica si un código de estudiante ya existe (excluyendo un ID específico)
     * @param codigo Código a verificar
     * @param idExcluir ID a excluir de la verificación
     * @return true si el código ya existe
     */
    public boolean existeCodigoEstudiante(double codigo, double idExcluir) {
        try {
            return EstudianteDao.existeCodigoEstudiante(codigo, idExcluir);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si un email ya existe (excluyendo un ID específico)
     * @param email Email a verificar
     * @param idExcluir ID a excluir de la verificación
     * @return true si el email ya existe
     */
    public boolean existeEmail(String email, double idExcluir) {
        try {
            return EstudianteDao.existeEmail(email, idExcluir);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si un estudiante existe en la base de datos
     * @param id ID del estudiante
     * @return true si el estudiante existe
     */
    public boolean existeEstudiante(double id) {
        try {
            return EstudianteDao.existeEstudiante(id);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene estadísticas básicas de los estudiantes
     * @return String con las estadísticas formateadas
     * @throws Exception si hay problemas de acceso a datos
     */
    public String obtenerEstadisticasEstudiantes() throws Exception {
        try {
            List<Estudiante> estudiantes = EstudianteDao.get();

            long totalEstudiantes = estudiantes.size();
            long estudiantesActivos = estudiantes.stream()
                    .mapToLong(e -> e.isActivo() ? 1 : 0)
                    .sum();
            long estudiantesInactivos = totalEstudiantes - estudiantesActivos;

            double promedioGeneral = estudiantes.stream()
                    .mapToDouble(Estudiante::getPromedio)
                    .average()
                    .orElse(0.0);

            return String.format("""
                Estadísticas de Estudiantes:
                
                Total de estudiantes: %d
                Estudiantes activos: %d
                Estudiantes inactivos: %d
                Promedio general: %.2f
                """,
                    totalEstudiantes,
                    estudiantesActivos,
                    estudiantesInactivos,
                    promedioGeneral
            );
        } catch (Exception e) {
            throw new Exception("Error al obtener estadísticas: " + e.getMessage(), e);
        }
    }

    /**
     * Busca estudiantes por nombre (nombres o apellidos)
     * @param nombre Texto a buscar en nombres o apellidos
     * @return Lista de estudiantes que coinciden con la búsqueda
     * @throws Exception si hay problemas de acceso a datos
     */
    public ObservableList<Object> buscarEstudiantesPorNombre(String nombre) throws Exception {
        try {
            List<Estudiante> todosLosEstudiantes = EstudianteDao.get();
            List<Estudiante> estudiantesEncontrados = todosLosEstudiantes.stream()
                    .filter(e ->
                            e.getNombres().toLowerCase().contains(nombre.toLowerCase()) ||
                                    e.getApellidos().toLowerCase().contains(nombre.toLowerCase())
                    )
                    .toList();

            return FXCollections.observableArrayList(estudiantesEncontrados.toArray());
        } catch (Exception e) {
            throw new Exception("Error al buscar estudiantes: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene estudiantes por programa
     * @param programaId ID del programa
     * @return Lista de estudiantes del programa especificado
     * @throws Exception si hay problemas de acceso a datos
     */
    public ObservableList<Object> obtenerEstudiantesPorPrograma(double programaId) throws Exception {
        try {
            List<Estudiante> todosLosEstudiantes = EstudianteDao.get();
            List<Estudiante> estudiantesDelPrograma = todosLosEstudiantes.stream()
                    .filter(e -> e.getPrograma() != null && e.getPrograma().getID() == programaId)
                    .toList();

            return FXCollections.observableArrayList(estudiantesDelPrograma.toArray());
        } catch (Exception e) {
            throw new Exception("Error al obtener estudiantes por programa: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene estudiantes por estado (activos/inactivos)
     * @param activo true para estudiantes activos, false para inactivos
     * @return Lista de estudiantes con el estado especificado
     * @throws Exception si hay problemas de acceso a datos
     */
    public ObservableList<Object> obtenerEstudiantesPorEstado(boolean activo) throws Exception {
        try {
            List<Estudiante> todosLosEstudiantes = EstudianteDao.get();
            List<Estudiante> estudiantesFiltrados = todosLosEstudiantes.stream()
                    .filter(e -> e.isActivo() == activo)
                    .toList();

            return FXCollections.observableArrayList(estudiantesFiltrados.toArray());
        } catch (Exception e) {
            throw new Exception("Error al obtener estudiantes por estado: " + e.getMessage(), e);
        }
    }

    /**
     * Genera un mensaje de validación personalizado
     * @param estudianteObj El objeto estudiante a validar
     * @return String con los errores de validación encontrados
     */
    public String obtenerMensajesValidacion(Object estudianteObj) {
        StringBuilder mensajes = new StringBuilder();

        if (!(estudianteObj instanceof Estudiante estudiante)) {
            mensajes.append("• El objeto no es un estudiante válido\n");
            return mensajes.toString();
        }

        if (estudiante.getNombres() == null || estudiante.getNombres().trim().isEmpty()) {
            mensajes.append("• Los nombres son obligatorios\n");
        }

        if (estudiante.getApellidos() == null || estudiante.getApellidos().trim().isEmpty()) {
            mensajes.append("• Los apellidos son obligatorios\n");
        }

        if (estudiante.getEmail() == null || estudiante.getEmail().trim().isEmpty()) {
            mensajes.append("• El email es obligatorio\n");
        } else if (!validarFormatoEmail(estudiante.getEmail())) {
            mensajes.append("• El formato del email no es válido\n");
        }

        if (estudiante.getCodigo() <= 0) {
            mensajes.append("• El código debe ser mayor a cero\n");
        }

        if (estudiante.getPromedio() < 0 || estudiante.getPromedio() > 5) {
            mensajes.append("• El promedio debe estar entre 0 y 5\n");
        }

        if (estudiante.getPrograma() == null) {
            mensajes.append("• Debe seleccionar un programa\n");
        }

        return mensajes.toString();
    }
}