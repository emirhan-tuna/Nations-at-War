const {getAvailableGame, setServerStatus} = require('./server/server.js');

let playerQueue = [];
let activeMatches = {};

const joinQueue = (userUid) => {
    if (!playerQueue.includes(userUid) && !activeMatches[userUid]) {
        playerQueue.push(userUid);
        matchmake();
    }
};

const matchmake = () => {
    if (playerQueue.length >= 2) {
        const server = getAvailableGame();
        
        if (!server) {
            console.log("no server available");
            return; 
        }

        const player1 = playerQueue.shift();
        const player2 = playerQueue.shift();
        setServerStatus(server.id, 'in-game');

        const matchData = {
            gameId: server.id,
            host: server.host,
            port: server.port
        };

        activeMatches[player1] = matchData;
        activeMatches[player2] = matchData;

        console.log(`matched ${player1} and ${player2} on server ${server.id}`);
    }
};

const getPlayerMatchStatus = (userUid) => {
    if (activeMatches[userUid]) {
        return {status: '0', server: activeMatches[userUid]};
    }
    if (playerQueue.includes(userUid)) {
        return {status: 1};
    }
    return {status: 2};
};

module.exports = {joinQueue, getPlayerMatchStatus};