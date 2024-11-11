package org.launchcode.techjobs.persistent.controllers;

import jakarta.persistence.Id;
import jakarta.validation.Valid;
import org.launchcode.techjobs.persistent.models.Employer;
import org.launchcode.techjobs.persistent.models.Job;
import org.launchcode.techjobs.persistent.models.Skill;
import org.launchcode.techjobs.persistent.models.data.EmployerRepository;
import org.launchcode.techjobs.persistent.models.data.JobRepository;
import org.launchcode.techjobs.persistent.models.data.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;


import java.util.List;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
public class HomeController {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobRepository jobRepository;

    @RequestMapping("/")
    public String index(Model model) {

        model.addAttribute("title", jobRepository.findAll());

        return "index";
    }

    @GetMapping("add")
    public String displayAddJobForm(Model model) {
	    model.addAttribute("title", "Add Job");
        model.addAttribute("employers", employerRepository.findAll());
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute(new Job());
        return "add";
    }

    @PostMapping("add")
    public String processAddJobForm(@ModelAttribute @Valid Job newJob,
                                       Errors errors, Model model,
                                    @RequestParam(required = false) int employerId,
                                    @RequestParam List<Integer> skills) {

        if (errors.hasErrors()) {
	        model.addAttribute("title", "Add Job");
            return "add";
        } else {

            Optional<Employer> job = employerRepository.findById(employerId);

            List<Skill> skillObjs = (List<Skill>) skillRepository.findAllById(skills);
            newJob.setSkills(skillObjs);

            if (job.isEmpty()) {
                model.addAttribute("title", "Invalid Event ID: " + employerId);
                model.addAttribute("employers", employerRepository.findAll());
                model.addAttribute("skills", skillRepository.findAllById(skills));
//            } else {
//                Employer employer = result.get();
//                model.addAttribute("employer", employer.getName());
//                return "employers/view";
            } else {

                Employer employer = job.get();
                newJob.setEmployer(employer);
                jobRepository.save(newJob);
            }
            }


        return "redirect:/";
    }

    @GetMapping("view/{jobId}")
    public String displayViewJob(Model model, @PathVariable int jobId) {

            return "view";
    }

}
