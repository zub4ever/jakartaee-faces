package com.exemplo.app.web;

import com.exemplo.app.model.Item;
import com.exemplo.app.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

/**
 * Backing bean da página inicial. @ViewScoped: vive enquanto o usuário
 * permanece na mesma página (sobrevive às requisições Ajax).
 */
@Named
@ViewScoped
public class ItemBean implements Serializable {

    @Inject
    private ItemRepository repository;

    private Item novo = new Item();
    private List<Item> itens;

    @PostConstruct
    void init() {
        carregar();
    }

    public void salvar() {
        repository.salvar(novo);
        novo = new Item();
        carregar();
        info("Item adicionado.");
    }

    public void remover(Item item) {
        repository.remover(item.getId());
        carregar();
        info("Item removido.");
    }

    private void carregar() {
        itens = repository.listar();
    }

    private void info(String msg) {
        FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    public Item getNovo() { return novo; }
    public List<Item> getItens() { return itens; }
}
