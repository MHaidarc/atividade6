package com.ifsp.carrinho;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CarrinhoControler {
    private List<Produto> produtos = new ArrayList<>();

    @GetMapping("/formCarrinho")
    public String formCarrinho() {

        return "formCarrinho.html";
    }

    @PostMapping("/cadastroCarrinho")
    public String cadastroCarrinhho(@RequestParam int id, @RequestParam String nome, @RequestParam double preco,
            @RequestParam int quantidade) {

        produtos.add(new Produto(id, nome, preco, quantidade));
        return "redirect:/carrinho";
    }

    @GetMapping("/carrinho")
    public String carrinho(Model model) {
        boolean vazio = produtos.isEmpty();
        int precoTotal = 0;
        int quantTotal = 0;

        for (Produto produto : produtos) {
            precoTotal += produto.getPreco() * produto.getQuantidade();
            quantTotal += produto.getQuantidade();
        }

        model.addAttribute("quantTotal", quantTotal);
        model.addAttribute("precoTotal", precoTotal);
        model.addAttribute("vazio", vazio);
        model.addAttribute("produtos", produtos);
        return "carrinho.html";
    }
}
