package com.example.DTO;

public class ProfesorDTO extends PersonaDTO {
    private String tipoContrato;
    public ProfesorDTO(double ID, String nombres, String apellidos, String email, String tipoContrato){
        super(ID, nombres, apellidos, email);
        this.tipoContrato = tipoContrato;
    }


    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }
}
