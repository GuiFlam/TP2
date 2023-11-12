package com.chat.serveur;

public class Invitation {
    private String aliasHote;
    private String aliasInvite;

    private String mode;

    public Invitation(String aliasHote, String aliasInvite, String mode) {
        this.aliasHote = aliasHote;
        this.aliasInvite = aliasInvite;
        this.mode = mode;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Invitation) {
            Invitation invitation = (Invitation) obj;
            return this.aliasInvite.equals(invitation.aliasInvite) && this.aliasHote.equals(invitation.aliasHote) && this.mode.equals(invitation.mode);
        }
        return false;
    }

    public String getAliasHote() {
        return aliasHote;
    }
    public String getAliasInvite() {
        return aliasInvite;
    }
    public String getMode() { return mode; }
}
