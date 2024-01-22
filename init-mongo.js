db.createUser(
    {
        user: "manoel",
        pwd: "manoel",
        roles: [
            {
                role: "readWrite",
                db: "pix"
            }
        ]
    }
);
db.createCollection("transacao_pix");