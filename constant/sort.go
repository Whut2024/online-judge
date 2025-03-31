package constant

import "fmt"

type Sort struct {
	PageSize  int    `json:"pageSize"`
	SortField string `json:"sortField"`
	SortOrder string `json:"sortOrder"`
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
	case "desc":
		*this = DESC
	default:
		return fmt.Errorf("invalid sort order: %s", text)
	}
	return nil
}
