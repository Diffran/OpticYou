package cat.ioc.opticyou.mapper;

import cat.ioc.opticyou.dto.HistorialDTO;
import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.service.ClientService;
import cat.ioc.opticyou.service.HistorialService;
import cat.ioc.opticyou.service.impl.ClientServiceImpl;
import cat.ioc.opticyou.service.impl.HistorialServiceImpl;


public class HistorialMapper {

    public static HistorialDTO toDto(Historial historial){
        if (historial == null) {
            return null;
        }
        HistorialDTO historialDTO = new HistorialDTO();
        historialDTO.setDataCreacio(historial.getDataCreacio());
        historialDTO.setIdhistorial(historial.getIdhistorial());
        historialDTO.setPatologies(historial.getPatologies());
        historialDTO.setIdClient(historial.getClient().getIdUsuari());
        return historialDTO;
    }

    public static Historial toEntity(HistorialDTO historialDTO, Client client){
        if (historialDTO == null) {
            return null;
        }
        Historial historial = new Historial();
        historial.setIdhistorial(historialDTO.getIdhistorial());
        historial.setDataCreacio(historialDTO.getDataCreacio());
        historial.setPatologies(historialDTO.getPatologies());
        historial.setClient(client);
        return historial;
    }
}
