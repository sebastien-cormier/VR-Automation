package fr.cormier.vra.web.services.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.cormier.domain.db.Command;
import fr.cormier.domain.db.CommandTypeEnum;
import fr.cormier.vra.service.ICommandService;
 

 
@Controller
@RequestMapping("api")
public class CommandController {
     
    ICommandService serviceCommand;
     
    @Autowired
    public CommandController(ICommandService service) {
        this.serviceCommand = service;
    }
 
    @RequestMapping("command8")
    @ResponseBody
    public Command getCommand() {
        return serviceCommand.getCommand(8);
    }
    
    @RequestMapping(value="commandmissing", params="userId", method=RequestMethod.GET)
    @ResponseBody
    public Map<CommandTypeEnum, List<String>> missingCommands(@RequestParam Long userId) {
    	Map<CommandTypeEnum, List<String>> results = serviceCommand.retrieveMissingCommands(userId.intValue(), 5);
    	
    	return results;	
    	//return serviceCommand.getCommand(10);
    }
    
    @RequestMapping(value="command", params="id")
    @ResponseBody
    public Command commandId(@RequestParam Long id) {
        return serviceCommand.getCommand(id.intValue());
    }
 
 
     
    // handles person form submit
    @RequestMapping(value="command", method=RequestMethod.POST)
    @ResponseBody
    public String addCommand(Command command) {
        serviceCommand.addCommand(command);
        return "Saved command: " + command.toString();
    }
}