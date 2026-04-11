const { auth } = require('./firebase.js');

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

module.exports = { checkAuth };