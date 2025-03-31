package response

// OrderItem 排序项
type OrderItem struct {
	Column    string `json:"column"`    // 排序列名
	Ascending bool   `json:"ascending"` // 是否升序
}

// Page 分页结构体
type Page struct {
	CountID          string         `json:"countId"`
	Current          int64          `json:"current"`
	MaxLimit         int64          `json:"maxLimit"`
	OptimizeCountSql bool           `json:"optimizeCountSql"`
	Orders           *[]OrderItem   `json:"orders"`
	Pages            int64          `json:"pages"`
	Records          *[]interface{} `json:"records"`
	SearchCount      bool           `json:"searchCount"`
	Size             int64          `json:"size"`
	Total            int64          `json:"total"`
}
