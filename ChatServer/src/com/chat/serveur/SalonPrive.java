package com.chat.serveur;
import com.echecs.PartieEchecs;

public class SalonPrive {
    private String aliasHote;
    private String aliasInvite;
    private PartieEchecs partieEchecs;

    public SalonPrive(String aliasHote, String aliasInvite) {
        setAliasHote(aliasHote);
        setAliasInvite(aliasInvite);
    }

    public void setPartieEchecs(PartieEchecs partieEchecs) {
        this.partieEchecs = partieEchecs;
    }
    public PartieEchecs getPartieEchecs() {
        return this.partieEchecs;
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
