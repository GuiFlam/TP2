package com.chat.serveur;

public class Invitation {
    private String aliasHote;
    private String aliasInvite;

    public Invitation(String aliasHote, String aliasInvite) {
        this.aliasHote = aliasHote;
        this.aliasInvite = aliasInvite;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Invitation) {
            Invitation invitation = (Invitation) obj;
            return this.aliasInvite.equals(invitation.aliasInvite) && this.aliasHote.equals(invitation.aliasHote);
        }
        return false;
    }

    public String getAliasHote() {
        return aliasHote;
    }
    public String getAliasInvite() {
        return aliasInvite;
    }
}
