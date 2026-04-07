const serverAuth = (req, res, next) => {
    const authHeader = req.headers.authorization;
    
    if (authHeader === "Bearer SERVER_SECRET_NOT_SO_SECRET_LIKE_THIS_THOUGH_HOPEFULLY_IWILL_USE_ENV_LATER") {
        return next()
    }
    
    console.log(`unauthorized attempt from ${req.ip}`);
    return res.status(401).json({error: 'unauthorized access'});
};

module.exports = {serverAuth}