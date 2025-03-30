package constant

import "fmt"

type UserRole string

const (
	Admin UserRole = "admin"
	User  UserRole = "user"
)

func (ur *UserRole) UnmarshalText(text []byte) error {
	switch string(text) {
	case "admin":
		*ur = Admin
	case "user":
		*ur = User
	default:
		return fmt.Errorf("invalid user role: %s", text)
	}
	return nil
}
