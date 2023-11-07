package com.chat.client;

import com.chat.client.EtatPartieEchecs;

/**
 * Cette classe étend la classe Client pour lui ajouter des fonctionnalités
 * spécifiques au chat et au jeu d'échecs en réseau.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class ClientChat extends Client {
    private EtatPartieEchecs etatPartieEchecs;

    public EtatPartieEchecs getEtatPartieEchecs() {
        return this.etatPartieEchecs;
    }
    public void setEtatPartieEchecs(EtatPartieEchecs etatPartieEchecs) {
        this.etatPartieEchecs = etatPartieEchecs;
    }

    public void nouvellePartie() {
        etatPartieEchecs = new EtatPartieEchecs();
    }
}
