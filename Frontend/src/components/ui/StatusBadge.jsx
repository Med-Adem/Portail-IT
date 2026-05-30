export default function StatusBadge({ status }) {
  const map = {
    OPEN:        'badge-open',
    IN_PROGRESS: 'badge-progress',
    CLOSED:      'badge-closed',
    PENDING:     'badge-pending',
    APPROVED:    'badge-approved',
    REJECTED:    'badge-rejected',
  }
  const labels = {
    OPEN: 'Open', IN_PROGRESS: 'In Progress', CLOSED: 'Closed',
    PENDING: 'Pending', APPROVED: 'Approved', REJECTED: 'Rejected',
  }
  return (
    <span className={map[status] || 'badge-closed'}>
      {labels[status] || status}
    </span>
  )
}
