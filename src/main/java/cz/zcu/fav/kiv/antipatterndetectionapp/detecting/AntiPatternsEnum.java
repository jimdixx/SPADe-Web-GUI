package cz.zcu.fav.kiv.antipatterndetectionapp.detecting;

public enum AntiPatternsEnum {

    TooLongSprint(1L,
            "TooLongSprint",
            "Too Long Sprint - DONE",
            "Iterations too long. (ideal iteration length is about 1-2 weeks, " +
                    "maximum 3 weeks). It could also be detected here if the length " +
                    "of the iteration does not change often (It can change at the " +
                    "beginning and at the end of the project, but it should not " +
                    "change in the already started project)."),

    VaryingSprintLength(2L,
            "VaryingSprintLength",
            "Varying Sprint Length - DONE",
            "The length of the sprint changes very often. " +
                    "It is clear that iterations will be different " +
                    "lengths at the beginning and end of the project, " +
                    "but the length of the sprint should not change " +
                    "during the project."),

    RoadToNowhere(3L,
            "RoadToNowhere",
            "Road To Nowhere - DONE",
            "The project is not sufficiently planned and therefore " +
                    "takes place on an ad hoc basis with an uncertain " +
                    "outcome and deadline. There is no project plan in the project."),

    SpecifyNothing(4L,
            "SpecifyNothing",
            "Specify Nothing - DONE",
            "The specification is not done intentionally. Programmers are " +
                    "expected to work better without written specifications."),

    BusinessAsUsual(5L,
            "BusinessAsUsual",
            "Business As Usual - DONE",
            "Absence of a retrospective after individual " +
                    "iterations or after the completion project."),

    IndifferentSpecialist(6L,
            "IndifferentSpecialist",
            "Indifferent specialist",
            "A team member who is a specialist in just one thing and does not want to learn new things. " +
                    "He refuses to learn new things outside of his specialization. It often disparages other " +
                    "\"technologies\". They reduce the seriousness of things that do not specialize in his specialization."),

    LongOrNonExistentFeedbackLoops(7L,
            "LongOrNonExistentFeedbackLoops",
            "Long Or Non-Existent Feedback Loops",
            "Long spacings between customer feedback or no feedback. The customer " +
                    "enters the project and sees the final result. In the end, the customer " +
                    "may not get what he really wanted. With long intervals of feedback, " +
                    "some misunderstood functionality can be created and we have to spend " +
                    "a lot of effort and time to redo it. ");




    public final Long id;
    public final String name;
    public final String printName;
    public final String description;

    AntiPatternsEnum(Long id, String name, String printName, String description) {
        this.id = id;
        this.name = name;
        this.printName = printName;
        this.description = description;
    }
}
