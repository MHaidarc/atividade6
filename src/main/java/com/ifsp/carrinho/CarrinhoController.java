package com.ifsp.carrinho;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CarrinhoController {
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

    public static double arredondar(double valor, int casas) {
        if (casas < 0)
            throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, casas);
        valor = valor * factor;
        long tmp = Math.round(valor);
        return (double) tmp / factor;
    }

    @GetMapping("/carrinho")
    public String carrinho(Model model) {
        boolean vazio = produtos.isEmpty();
        double precoTotal = 0.0;
        double quantTotal = 0.0;

        for (Produto produto : produtos) {
            precoTotal += produto.getPreco() * produto.getQuantidade();
            quantTotal += produto.getQuantidade();
        }

        model.addAttribute("quantTotal", quantTotal);
        model.addAttribute("precoTotal", arredondar(precoTotal, 2));
        model.addAttribute("vazio", vazio);
        model.addAttribute("produtos", produtos);
        return "carrinho.html";
    }

    @PostMapping("/removerProduto")
    public String removerProduto(@RequestParam int id) {
        Iterator<Produto> produtoIterator = produtos.iterator();

        while (produtoIterator.hasNext()) {
            Produto nextProduto = produtoIterator.next();
            if (nextProduto.getId() == id) {
                produtoIterator.remove();
            }
        }
        return "redirect:/carrinho";
    }

    @PostMapping("/addQuant")
    public String addQuant(@RequestParam int id) {

        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                produto.setQuantidade(produto.getQuantidade() + 1);
            }
        }

        return "redirect:/carrinho";
    }

    @PostMapping("/rmQuant")
    public String rmQuant(@RequestParam int id) {
        boolean deveRemover = false;
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                produto.setQuantidade(produto.getQuantidade() - 1);

                if (produto.getQuantidade() == 0) {
                    deveRemover = true;
                }
            }
        }
        if (deveRemover)
            removerProduto(id);

        return "redirect:/carrinho";
    }

    @PostMapping("/rmCarrinho")
    public String postMethodName() {
        produtos.clear();
        return "redirect:/carrinho";
    }

}
