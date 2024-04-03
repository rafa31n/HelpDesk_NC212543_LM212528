package com.example.nc212543_lm212528

import java.util.Date

class Ticket {
    var numTicket: Int? = 0
    var titulo: String? = null
    var descripcion: String? = null
    var departamento: String? = null
    var autorTicket: String? = null
    var emailAutor: String? = null
    var fechaCreacion: String? = null
    var estado: String? = null
    var fechaFinalizacion: String? = null

    constructor(
        numTicket: Int?, titulo: String?, descripcion: String?,
        departamento: String?, autorTicket: String?, emailAutor: String?, fechaCreacion: String?,
        estado: String?, fechaFinalizacion: String?
    ) {
        this.numTicket = numTicket
        this.titulo = titulo
        this.descripcion = descripcion
        this.departamento = departamento
        this.autorTicket = autorTicket
        this.emailAutor = emailAutor
        this.fechaCreacion = fechaCreacion
        this.estado = estado
        this.fechaFinalizacion = fechaFinalizacion
    }
}