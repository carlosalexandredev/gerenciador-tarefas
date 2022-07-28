package com.example.demo.controller;

import com.example.demo.model.bo.TarefasBO;
import com.example.demo.model.bo.UsuarioBO;
import com.example.demo.model.bo.exceptionhandler.PessoaInexistenteOuInativaException;
import com.example.demo.model.dto.tarefas.TarefaDTO;
import com.example.demo.model.dto.usuario.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UsuarioBO usuarioBO;

    @Autowired
    private TarefasBO tarefaBO;

    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value="/funcionario",  method=RequestMethod.GET)
    public String index(){return "funcionario";}

    @RequestMapping(value="/admin",  method=RequestMethod.GET)
    public String admin(){
        return "admin";
    }

    /** Métodos para Funcionário*/

    @RequestMapping(value="/admin-funcionario", method=RequestMethod.GET)
    public String admin_func(ModelMap model){
        List<UsuarioDTO> funcionarios = usuarioBO.buscaAll();
        model.addAttribute("funcionarios", funcionarios);
        return "admin-funcionario";
    }

    @RequestMapping(value="/salva-funcionario", method=RequestMethod.POST)
    public String salvarPessoa(@Valid @ModelAttribute UsuarioDTO funcionario, BindingResult result, RedirectAttributes attributes, HttpServletResponse response){
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/admin-funcionario";
        }
        UsuarioDTO user = usuarioBO.criarUser(funcionario, response);
        return "redirect:/admin-funcionario";
    }

    @RequestMapping(value="/access-denied", method=RequestMethod.GET)
    public String access(){
        return "access-denied";
    }

//    @RequestMapping(value="/deletar-funcionario/{codigo}", method=RequestMethod.DELETE)
//    @ResponseBody
//    public String delete(@PathVariable Long codigo){
//        usuarioBO.removerUser(codigo);
//        return "redirect:/admin-funcionario";
//    }

    @RequestMapping(value = "/delete_funcionario", method = RequestMethod.GET)
    public String handleDeleteUser(@RequestParam(name="codigo")Long codigo) {
        usuarioBO.removerUser(codigo);

        return "redirect:/admin-funcionario";
    }

    @PostMapping("/update/{codigo}")
    public String updateUser(@PathVariable("codigo") long codigo, @Valid UsuarioDTO funcionario,
                             BindingResult result, Model model) throws PessoaInexistenteOuInativaException {
        if (result.hasErrors()) {
            funcionario.setCodigo(codigo);
            return "admin-funcionario";
        }
        usuarioBO.atualizaUser(codigo, funcionario);
        return "redirect:/admin-funcionario";
    }








    /** Métodos para Tarefas*/
    @RequestMapping(value="/admin/tarefas", method=RequestMethod.GET)
    public String admin_tarefas(ModelMap model){
        List<TarefaDTO> tarefas = tarefaBO.buscaTarefas();
        model.addAttribute("tarefas", tarefas);
        return "admin";
    }

    @RequestMapping(value="/salva-tarefa", method=RequestMethod.POST)
    public String salvarTarefa(@Valid @ModelAttribute TarefaDTO tarefa, BindingResult result, RedirectAttributes attributes, HttpServletResponse response) throws PessoaInexistenteOuInativaException {
        if(result.hasErrors()){
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/admin";
        }
        TarefaDTO tarefasalva = tarefaBO.criarTarefa(tarefa, response);
        return "redirect:/admin";
    }

//    @RequestMapping(value="/deletar-tarefa/{codigo}", method=RequestMethod.DELETE)
//    @ResponseBody
//    public String deleteTarefa(@PathVariable Long codigo){
//        tarefaBO.removerTarefas(codigo);
//        return "redirect:/admin-funcionario";
//    }
//
//    @RequestMapping(value = "/delete_tarefa", method = RequestMethod.GET)
//    public String handleDeleteTarefa(@RequestParam(name="codigo")Long codigo) {
//        usuarioBO.removerUser(codigo);
//
//        return "redirect:/admin-funcionario";
//    }
//
//    @PostMapping("/update-tarefa/{codigo}")
//    public String updateTarefa(@PathVariable("codigo") long codigo, @Valid UsuarioDTO funcionario,
//                             BindingResult result, Model model) throws PessoaInexistenteOuInativaException {
//        if (result.hasErrors()) {
//            funcionario.setCodigo(codigo);
//            return "admin-funcionario";
//        }
//        usuarioBO.atualizaUser(codigo, funcionario);
//        return "redirect:/admin-funcionario";
//    }

}
