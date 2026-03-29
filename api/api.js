const express = require('express');
const app = express();

const PORT = process.env.port || 3000;

app.get('/aaa', (req,res) => {
    res.send("suces");
});

app.listen(PORT, "0.0.0.0", () => {
    console.log("server port: " + PORT);
});

