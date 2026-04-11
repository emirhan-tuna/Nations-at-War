const serverAuth = (req, res, next) => {
    const authHeader = req.headers.authorization;
    if (authHeader === `Bearer ${process.env.SERVER_SECRET}`) {
        return next();
    }
    console.log(`unauthorized attempt from ${req.ip}`);
    return res.status(401).json({error: 'unauthorized server access'});
};

const activeGames = new Map(); 

const registerServer = (gameId, host, port) => {
    activeGames.set(gameId, {
        id: gameId,
        host: host,
        port: port,
        lastHeartbeat: Date.now(),
        status: 1
    });
};

const updateHeartbeat = (gameId, host, port) => {
    if (activeGames.has(gameId)) {
        const game = activeGames.get(gameId);
        game.lastHeartbeat = Date.now();
        game.host = host;
        game.port = port;
    } else {
        registerServer(gameId, host, port);
    }
};

const getAvailableGame = () => {
    for (let [id, game] of activeGames.entries()) {
        if (game.status === 1) {
            return game;
        }
    }
    return null;
};

const setServerStatus = (gameId, status) => {
    if (activeGames.has(gameId)) {
        activeGames.get(gameId).status = status;
    }
};

module.exports = { 
    serverAuth, 
    registerServer, 
    updateHeartbeat, 
    getAvailableGame,
    setServerStatus 
};