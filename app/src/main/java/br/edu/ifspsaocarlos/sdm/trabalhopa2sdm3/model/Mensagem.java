package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.model;

import java.io.Serializable;

/**
 * Created by Note on 03/09/2017.
 */

@Entity
public class Mensagem implements Serializable {

    static final long serialVersionUID = 1L;

    @Expose
    @Id
    private Long id;

    @Expose
    private String assunto;

    @Expose
    private String corpo;

    @Expose
    private Long origem_id;

    @Expose
    private Long destino_id;

    @ToOne(joinProperty = "origem_id")
    private Contato origem;

    @ToOne(joinProperty = "destino_id")
    private Contato destino;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 856867892)
    private transient MensagemDao myDao;

    @Generated(hash = 1518644232)
    private transient Long origem__resolvedKey;

    @Generated(hash = 181593545)
    private transient Long destino__resolvedKey;

    @Generated(hash = 1041379335)
    public Mensagem() {
    }

    @Generated(hash = 838567892)
    public Mensagem(Long id, String assunto, String corpo, Long origem_id, Long destino_id) {
        this.id = id;
        this.assunto = assunto;
        this.corpo = corpo;
        this.origem_id = origem_id;
        this.destino_id = destino_id;
    }

    @Override
    public String toString() {
        return corpo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public Long getOrigem_id() {
        return origem_id;
    }

    public void setOrigem_id(Long origem_id) {
        this.origem_id = origem_id;
    }

    public Long getDestino_id() {
        return destino_id;
    }

    public void setDestino_id(Long destino_id) {
        this.destino_id = destino_id;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2079858704)
    public Contato getOrigem() {
        Long __key = this.origem_id;
        if (origem__resolvedKey == null || !origem__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ContatoDao targetDao = daoSession.getContatoDao();
            Contato origemNew = targetDao.load(__key);
            synchronized (this) {
                origem = origemNew;
                origem__resolvedKey = __key;
            }
        }
        return origem;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 156048779)
    public void setOrigem(Contato origem) {
        synchronized (this) {
            this.origem = origem;
            origem_id = origem == null ? null : origem.getId();
            origem__resolvedKey = origem_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 740808671)
    public Contato getDestino() {
        Long __key = this.destino_id;
        if (destino__resolvedKey == null || !destino__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ContatoDao targetDao = daoSession.getContatoDao();
            Contato destinoNew = targetDao.load(__key);
            synchronized (this) {
                destino = destinoNew;
                destino__resolvedKey = __key;
            }
        }
        return destino;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1820623170)
    public void setDestino(Contato destino) {
        synchronized (this) {
            this.destino = destino;
            destino_id = destino == null ? null : destino.getId();
            destino__resolvedKey = destino_id;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1383398240)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMensagemDao() : null;
    }
}
