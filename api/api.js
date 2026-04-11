require('dotenv').config();

const express = require('express');
const {db} = require('./user/firebase.js');
const {checkAuth} = require('./user/authenticate.js');
const {registerServer, updateHeartbeat, serverAuth} = require('./server/server.js');
const {joinQueue, getPlayerMatchStatus} = require('./matchmaker.js');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

app.get('/', (req, res) => {
    res.send("api is running");
});

app.get('/my-profile', checkAuth, async (req, res) => {
    const userUid = req.user.uid;

    try {
        const userRef = db.collection('users').doc(userUid);
        const userDoc = await userRef.get();

        if (!userDoc.exists) {
            return res.status(404).json({ 
                success: false, 
                message: "no database entry found" 
            });
        }

        const userData = userDoc.data();

        res.status(200).json({ 
            success: true,
            uid: userUid,
            profile: userData
        });

    } catch (error) {
        console.error('error fetching profile:', error);
        res.status(500).json({ error: "Internal server error" });
    }
});

app.post('/update-profile', checkAuth, async (req, res) => {
    const userUid = req.user.uid;
    const username = req.body.username; 

    let updates = {};
    if (username) updates.username = username;

    if (Object.keys(updates).length === 0) {
         return res.status(400).json({error: "nothing to update"});
    }

    try {
        const userRef = db.collection('users').doc(userUid);
        await userRef.update(updates);

        res.status(200).json({success: true, message: "profile updated/created"});

    } catch (error) {
        res.status(500).json({error: "failed to update"});
    }
});

//matchmaker

app.post('/matchmake/join', checkAuth, (req, res) => {
    joinQueue(req.user.uid);
    res.status(200).json({message: "added to matchmaking queue"});
});

app.get('/matchmake/status', checkAuth, (req, res) => {
    const status = getPlayerMatchStatus(req.user.uid);
    res.status(200).json(status);
});

//server

//check server

app.get('/hello', (req, res) => {
    if(serverActive != null) {
        res.status(200).json(serverActive);
    } else {
        res.status(500).json({error: "no server"});
    }
})

app.post('/reserve', serverAuth, (req, res) => {
    const {host, port, gameId} = req.body;
    registerServer(gameId, host, port);
    res.status(204).send();
})

app.post('/heartbeat', serverAuth, (req, res) => {
    const {host, port, gameId} = req.body;
    updateHeartbeat(gameId, host, port);
    res.status(204).send();
});


app.listen(PORT, () => {
    console.log(`api listening on port: ${PORT}`);
});