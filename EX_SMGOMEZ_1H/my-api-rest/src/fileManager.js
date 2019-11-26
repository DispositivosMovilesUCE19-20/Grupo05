//modulo para manejar los archivos 
const fs = require('fs');
/**
 * Funcion que realiza la creacion del archivo en un carpeta específica.
 * 
 */
var encabezado ='<?xml version="1.0" encoding="UTF-8"?>\n';
  module.exports=function(nombre, extension,contenido){
    fs.writeFile('datosEstudiante/'+nombre+'.'+extension, encabezado + contenido,'utf8', (err) => {
        if (err) throw err;
        console.log('Archivo Creado Satisfactoriamente');
      });

  }

 