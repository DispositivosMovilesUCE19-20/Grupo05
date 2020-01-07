
//para usar express y levantar un 'servidor'
const express = require('express');
//path
const path = require("path");
//modulo para transoportar json en el body de la peticion http
const bodyParser = require('body-parser');
const app = express();
//modulo para el parseo de json a xml
const jsonxml = require('jsontoxml');
//Establecemos el tipo de codificacion y el objeto a parsear
//cors
const cors = require('cors')

const request = require('request');
const async = require('async');

//exportando el fileManager
const file = require('./src/fileManager.js');
const {pool} = require('./postgresdb/psqlController');
//puerto en el que el servicio se levantará localmente
//conexion a posgres
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(cors())
app.set("views", path.join(__dirname, "views"));
app.set("view engine", "ejs");


var port = '5000';
var myJson={};

//aqui se define el endpoint o el servicio web
app.get('/grupo05/mensaje', (request, response) => {

    response.status(200).send({ 'msg': 'SERVICIO GRUPO 05' });
    //response.json({ 'msg': 'SERVICIO GRUPO 05' });
})

app.get('/listarLogs', (request, response) => {
    pool.connect()
pool.query('SELECT * FROM logs').then(res => {
       console.log(res.rows)
            let data =res.rows;
        
        response.status(200).send({data})
    })
    .catch(err => {
        
    })

    
})

//home
app.get('/', (request, response) => {
    pool.connect()
pool.query('SELECT * FROM logs ORDER BY clog DESC').then(res => {
       console.log(res.rows)
            let log =res.rows;
        
        //response.status(200).send({data})
        response.render("logs", {
            data: log
          });
    })
    .catch(err => {
        
    })

    
})

app.post('/saveLogs', (request, response) => {
    console.log(request.body)
    var fecha = new Date().toLocaleString();
    
    let insertQuery = `INSERT INTO logs(usuario,modelo,nombre,version,created) VALUES('${request.body.usuario}','${request.body.modelo}','${request.body.nombre}','${request.body.version}','${fecha}')`
    console.log(insertQuery)
pool.connect()
  pool.query(insertQuery).then(res => {
         console.log(res.rows)
          response.status(200).send({message:"Logs Guardados Correctamente"})
          //pool.end()
      })
      .catch(err => {
          if (err) {
            response.status(500).send({message:"algo salio mal"})
            //pool.end()
            return console.log(err)
          }
          
      })
    
})

app.get('/listarLogss', (request, response) => {
        //response.json({ 'msg': 'SERVICIO GRUPO 05' });
})

//aqui se define el endpoint o el servicio web
app.get('/grupo05/examen', (request, response) => {
    response.status(200).send({ 'mensajeExamen': 'Examen Optativa III' });
})

//aqui se define el endpoint o el servicio web
app.get('/grupo05/loginMsg', (request, response) => {
    response.status(200).send({"loginExamen":"Bienvenido al Examen"});
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
app.listen(process.env.PORT || port, () => {
    console.log('Escuchando en el puerto: ' + port);

})

//servicio para recibir el json 
app.put('/toXml',  (req, res)=> {

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