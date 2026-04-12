require('dotenv').config();

const express = require('express');
const {db, admin} = require('./user/firebase.js');
const {checkAuth} = require('./user/authenticate.js');
const {registerServer, updateHeartbeat, serverAuth, setServerStatus, removeServer} = require('./server/server.js');
const {joinQueue, getPlayerMatchStatus, endMatchByServerId} = require('./matchmaker.js');

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

app.post('/reserve', serverAuth, (req, res) => {
    const {host, port, gameId} = req.body;
    registerServer(gameId, host, port);
    res.status(204).send();
})

app.post('/heartbeat', serverAuth, (req, res) => {
    const {host, port, gameId} = req.body;
    const success = updateHeartbeat(gameId, host, port);

    if(success) {
        res.status(204).send();
    } else {
        res.status(404).send();
    }
});

app.post('/end-match', serverAuth, (req, res) => {
    const {gameId} = req.body;

    if (!gameId) {
        return res.status(400).json({error: "missing gameId"});
    }

    endMatchByServerId(gameId);

    removeServer(gameId);

    console.log(`game ${gameId} ended. server and players freed.`);
    res.status(200).send();
});

app.post('/update-stats', serverAuth, async (req, res) => {
    const { winnerId, loserId } = req.body;

    try {
        const batch = db.batch();

        if (winnerId && winnerId.trim() !== "") {

        const winner = db.collection('users').doc(winnerId);
            batch.set(winner,
                {
                    playedGames: admin.firestore.FieldValue.increment(1),
                    wins: admin.firestore.FieldValue.increment(1)
                },
                {merge: true}
            );
        }

        if (loserId && loserId.trim() !== "") {
            const loser = db.collection('users').doc(loserId);
            batch.set(loser,
                {
                    playedGames: admin.firestore.FieldValue.increment(1)
                },
                {merge: true}
            );
        }

        await batch.commit();
        console.log(`stats updated, winner: ${winnerId}, loser: ${loserId}`);
        res.status(200).send();
    } catch (error) {
        console.error('failed to update stats:', error);
        res.status(500).json({ error: "failed to update database with game stats" });
    }
});


app.listen(PORT, () => {
    console.log(`api listening on port: ${PORT}`);

    db.collection('users').limit(1).get()
        .then(() => console.log('firebase starting.')) //doesnt automatically do the ssl handshake upon launching for some reasonb
        .catch(err => console.error('firebase fail:', err));
});