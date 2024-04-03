package com.example.nc212543_lm212528

class Usuario {
    var nombre: String? = null
    var correo: String? = null
    var password: String? = null
    var rol: String? = null

    constructor(nombre: String?, correo: String?, password: String?, rol: String?) {
        this.nombre = nombre
        this.correo = correo
        this.password = password
        this.rol = rol
    }
}