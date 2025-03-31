package router

type RouterGroup struct {
	UserRouter
	QuestionRouterGroup
	SubmissionRouterGroup
}

var (
	RouterGroupObj = new(RouterGroup)
)
