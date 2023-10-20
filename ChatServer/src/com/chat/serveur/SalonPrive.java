package com.chat.serveur;

public class SalonPrive {
    private String aliasHote;
    private String aliasInvite;

    public SalonPrive(String aliasHote, String aliasInvite) {
        this.aliasHote = aliasHote;
        this.aliasInvite = aliasInvite;
    }


    public void setAliasHote(String aliasHote) {
        this.aliasHote = aliasHote;
    }
    public void setAliasInvite(String aliasInvite) {
        this.aliasInvite = aliasInvite;
    }
    public String getAliasHote() {
        return aliasHote;
    }
    public String getAliasInvite() {
        return aliasInvite;
    }
}
