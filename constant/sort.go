package constant

import "fmt"

type Sort struct {
	PageSize  int       `json:"pageSize" binding:"required,max=20"`
	SortField string    `json:"sortField"`
	SortOrder SortOrder `json:"sortOrder"`
	Current   int       `json:"current"`
}

type SortOrder string

const (
	ASC  SortOrder = "asc"
	DESC SortOrder = "desc"
)

func (this *SortOrder) UnmarshalText(text []byte) error {
	switch string(text) {
	case "asc":
		*this = ASC
	case "descend":
		*this = DESC
	default:
		return fmt.Errorf("invalid sort order: %s", text)
	}
	return nil
}
