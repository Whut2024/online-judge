package api

type ApiGroup struct {
	UserApi
	QuestionApi
	SubmissionApi
}

var ApiGroupObj = new(ApiGroup)
