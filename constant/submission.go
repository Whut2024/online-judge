package constant

import "fmt"

type Language string

const (
	JAVA Language = "java"
	GO   Language = "go"
)

func (this *Language) UnmarshalText(text []byte) error {
	switch string(text) {
	case "java":
		*this = JAVA
	case "go":
		*this = GO
	default:
		return fmt.Errorf("invalid language: %s", text)
	}
	return nil
}

type SubmissionStatus int

const (
	Error SubmissionStatus = iota
	Over
	Running
)

func (s SubmissionStatus) String() string {
	return [...]string{"异常", "完成", "运行中"}[s]
}

func (s SubmissionStatus) Value() int {
	return int(s)
}
