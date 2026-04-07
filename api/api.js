require('dotenv').config();

const express = require('express');
// Import your Firebase Admin configuration
const {auth, db} = require('./user/firebase');
const {serverAuth} = require('./server/server.js')

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

const checkAuth = async (req, res, next) => {
    const authHeader = req.headers.authorization;

    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        return res.status(401).json({error: 'unauthorized: missing/malformed token'});
    }

    const idToken = authHeader.split('Bearer ')[1];

    try {
        const decodedToken = await auth.verifyIdToken(idToken);
        req.user = decodedToken; 
        next(); 
    } catch (error) {
        console.error('token verification failed:', error.message);
        return res.status(403).json({error: 'unauthorized: invalid/expired token'});
    }
};

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

//srv

let serverActive = null;

const setActive = function(active) {
    serverActive = active;
};

const getActive = function() {
    return serverActive;
};

//check server

app.get('/hello', (req, res) => {
    if(serverActive != null) {
        res.status(200).json(serverActive);
    } else {
        res.status(500).json({error: "no server"});
    }
})

app.use(serverAuth);

app.post('/reserve', (req, res) => {
    const {host, port, gameId} = req.body;

    if(!host || !port || !gameId) {
        return res.status(400).json({error: "missing host/port/gameId"});
    }

    setActive({
        id: gameId,
        host: host,
        port: port,
        lastHeartbeat: Date.now(),
        status: 1
    });

    res.status(204).send();
})

app.post('/heartbeat', (req, res) => {
    const {host, port} = req.body
    const currentServer = getActive();
    if(currentServer == null) {
        setActive({
            id: 0,
            host: null,
            port: null,
            lastHeartbeat: Date.now(),
            status: 0
        });
    } else {
        currentServer.host = host;
        currentServer.port = port;
        currentServer.lastHeartbeat = Date.now();
    }
    
    res.status(204).send(); 
});



app.listen(PORT, () => {
    console.log(`api listening on port: ${PORT}`);
});