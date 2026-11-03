package com.exemplo.app.repository;

import com.exemplo.app.model.Item;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ItemRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Item> listar() {
        return em.createQuery("select i from Item i order by i.criadoEm desc", Item.class)
                 .getResultList();
    }

    @Transactional
    public Item salvar(Item item) {
        em.persist(item);
        return item;
    }

    @Transactional
    public void remover(Long id) {
        Item item = em.find(Item.class, id);
        if (item != null) {
            em.remove(item);
        }
    }
}
