
//para usar express y levantar un 'servidor'
const express = require('express');
//modulo para transoportar json en el body de la peticion http
const bodyParser = require('body-parser');
const app = express();
//modulo para el parseo de json a xml
const jsonxml = require('jsontoxml');
//Establecemos el tipo de codificacion y el objeto a parsear
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
const request = require('request');
const async = require('async');
//exportando el fileManager
const file = require('./src/fileManager.js');
//puerto en el que el servicio se levantará localmente
var port = '5000';
var myJson={};

//aqui se define el endpoint o el servicio web
app.get('/grupo05/mensaje', (request, response) => {
    response.json({ 'msg': 'SERVICIO GRUPO 05' });
})

//aqui se define el endpoint o el servicio web
app.get('/grupo05/editado', (request, response) => {
    response.json({ 'msgEdit': 'El Estudiante se ha editado correctamente' });
})

//aqui se define el endpoint o el servicio web
app.get('/grupo05/eliminado', (request, response) => {
    response.json({ 'msgDelete': 'El Estudiante se ha eliminado correctamente' });
})

//aqui se define el endpoint o el servicio web
app.get('/grupo05/error', (request, response) => {
    response.json({
        'msgErrorEdit': 'Se produjo un problema en la Edición',
        'msgErrorDelete': 'Se produjo un problema en la Eliminación'
    });
})

//aqui se define el endpoint o el servicio web
app.get('/grupo05/mensajes', (request, response) => {
    response.json({
        'error': 'ERROR, ALGO SALIÓ MAL',
        'borrado': 'El Estudiante se ha eliminado correctamente',
        'editado': 'El Estudiante se ha editado correctamente'
    });
})

//aqui se levanta el servidor en el puerto 8010
app.listen(process.env.PORT || 5000, () => {
    console.log('Escuchando en el puerto: ' + port);

})

//servicio para recibir el json 
app.put('/toXml', function (req, res) {

    if (req.body) {
        respuesta = {
            error: false,
            codigo: 200,
            mensaje: 'Recibido',
            respuesta: req.body
        }

    } else {
        respuesta = {
            error: true,
            codigo: 502,
            mensaje: 'Body Vacío'
        };
        return;
    }
   
   var jj=req.body;

	console.log(jj);    
    var resultado = jsonxml (jj);    
	console.log(resultado);
    file(nombre='estudiantes',extension='xml',contenido=resultado);    
    res.send({response:resultado});
    
});

//funcion para parsear de JSON a XML
//var resultado = parser(o=myJson, tab="");
//console.log(resultado);
//Funcion para guardar el archivo en el servidor
//file(nombre='mi_archivo',extension='xml',contenido=resultado);