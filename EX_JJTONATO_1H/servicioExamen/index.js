const express = require("express");
const app = express();

let mensaje = {
 msg:'Examen Primer Hemisemestre'
};


app.get('/', function (req, res) {
 
res.send(mensaje);


});app.listen(3000, () => {
 console.log("El servidor está inicializado en el puerto 3000");
});