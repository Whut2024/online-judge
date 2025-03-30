package service

type ServiceGroup struct {
	QuestionService
	SubmissionService
	UserService
}

var ServiceGroupObj = new(ServiceGroup)
