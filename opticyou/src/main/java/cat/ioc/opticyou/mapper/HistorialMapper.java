package cat.ioc.opticyou.mapper;

import cat.ioc.opticyou.dto.HistorialDTO;
import cat.ioc.opticyou.model.Client;
import cat.ioc.opticyou.model.Historial;
import cat.ioc.opticyou.service.impl.ClientServiceImpl;


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

    public static Historial toEntity(HistorialDTO historialDTO, ClientServiceImpl clientService){
        if (historialDTO == null) {
            return null;
        }
        Historial historial = new Historial();
        historial.setIdhistorial(historialDTO.getIdhistorial());
        historial.setDataCreacio(historialDTO.getDataCreacio());
        historial.setPatologies(historialDTO.getPatologies());
        Client client = clientService.getClientById(historialDTO.getIdClient());
        historial.setClient(client);
        return historial;
    }
}
