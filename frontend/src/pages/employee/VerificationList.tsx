import React, { FC, useState } from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  TablePagination,
  Typography,
  Chip,
  IconButton,
  Box,
  CircularProgress,
  Alert,
  TableSortLabel,
  TextField,
  MenuItem,
  FormControl,
  InputLabel,
  Select,
  Grid,
  Button,
  Collapse,
} from '@mui/material';
import { 
  Refresh as RefreshIcon, 
  Visibility as VisibilityIcon,
  CheckCircle as CheckCircleIcon,
  Error as ErrorIcon,
  Pending as PendingIcon,
  Help as HelpIcon,
  FilterList as FilterIcon,
  Clear as ClearIcon
} from '@mui/icons-material';
import { useTranslation } from 'react-i18next';
import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { getAllVerifications } from '../../api/verification';
import { VerificationStatus, VerificationSortBy, SortOrder, DocumentType } from '../../types/api';

const VerificationList: FC = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [sortBy, setSortBy] = useState<VerificationSortBy>('START_DATE');
  const [sortOrder, setSortOrder] = useState<SortOrder>('DESC');
  const [showFilters, setShowFilters] = useState(false);

  const [filters, setFilters] = useState({
    firstName: '',
    lastName: '',
    documentNumber: '',
    documentType: '' as DocumentType | '',
    status: '' as VerificationStatus | '',
    nationality: '',
    email: '',
    phoneNumber: '',
    startDateFrom: '',
    startDateTo: ''
  });

  const { data, isLoading, isError, error, refetch } = useQuery({
    queryKey: ['verifications', page, pageSize, sortBy, sortOrder, filters],
    queryFn: () => getAllVerifications({
      'paginationFilter.page': page,
      'paginationFilter.pageSize': pageSize,
      sortBy,
      sortOrder,
      statuses: filters.status ? [filters.status] : undefined,
      'clientFilters.firstName': filters.firstName || undefined,
      'clientFilters.lastName': filters.lastName || undefined,
      'clientFilters.nationality': filters.nationality || undefined,
      'clientFilters.email': filters.email || undefined,
      'clientFilters.phoneNumber': filters.phoneNumber || undefined,
      'identityDocumentFilters.identityDocumentTypes': filters.documentType ? [filters.documentType as DocumentType] : undefined,
      'identityDocumentFilters.number': filters.documentNumber || undefined,
      'startDate.from': filters.startDateFrom ? new Date(filters.startDateFrom).toISOString() : undefined,
      'startDate.to': filters.startDateTo ? new Date(filters.startDateTo).toISOString() : undefined,
    }),
  });

  const handleChangePage = (_: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPageSize(parseInt(event.target.value, 10));
    setPage(0);
  };

  const handleSort = (property: VerificationSortBy) => {
    const isAsc = sortBy === property && sortOrder === 'ASC';
    setSortOrder(isAsc ? 'DESC' : 'ASC');
    setSortBy(property);
    setPage(0);
  };

  const handleFilterChange = (field: string, value: string) => {
    setFilters(prev => ({ ...prev, [field]: value }));
    setPage(0);
  };

  const clearFilters = () => {
    setFilters({
      firstName: '',
      lastName: '',
      documentNumber: '',
      documentType: '',
      status: '',
      nationality: '',
      email: '',
      phoneNumber: '',
      startDateFrom: '',
      startDateTo: ''
    });
    setPage(0);
  };

  const getStatusIcon = (status: VerificationStatus) => {
    switch (status) {
      case 'VERIFIED':
        return <CheckCircleIcon fontSize="small" />;
      case 'FAILED':
        return <ErrorIcon fontSize="small" />;
      case 'IN_PROGRESS':
        return <PendingIcon fontSize="small" />;
      case 'AWAITING_FINAL_DECISION':
        return <PendingIcon fontSize="small" color="info" />;
      default:
        return <HelpIcon fontSize="small" />;
    }
  };

  const getStatusColor = (status: VerificationStatus) => {
    switch (status) {
      case 'VERIFIED':
        return 'success';
      case 'FAILED':
        return 'error';
      case 'IN_PROGRESS':
        return 'warning';
      case 'AWAITING_FINAL_DECISION':
        return 'info';
      default:
        return 'default';
    }
  };

  const formatDate = (dateString?: string) => {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleString();
  };

  return (
    <Box sx={{ mt: 4, width: '100%' }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2, width: '100%' }}>
        <Typography variant="h5">
          {t('employee.verificationList.title')}
        </Typography>
        <Box>
          <Button 
            startIcon={<FilterIcon />} 
            onClick={() => setShowFilters(!showFilters)}
            sx={{ mr: 1 }}
          >
            {t('employee.verificationList.filters.title')}
          </Button>
          <IconButton onClick={() => refetch()} disabled={isLoading}>
            <RefreshIcon />
          </IconButton>
        </Box>
      </Box>

      <Collapse in={showFilters}>
        <Paper sx={{ p: 2, mb: 3 }}>
          <Grid container spacing={2}>
            <Grid size={{ xs: 12, sm: 6, md: 3 }}>
              <TextField
                fullWidth
                label={t('employee.verificationList.filters.firstName')}
                value={filters.firstName}
                onChange={(e) => handleFilterChange('firstName', e.target.value)}
                size="small"
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6, md: 3 }}>
              <TextField
                fullWidth
                label={t('employee.verificationList.filters.lastName')}
                value={filters.lastName}
                onChange={(e) => handleFilterChange('lastName', e.target.value)}
                size="small"
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6, md: 3 }}>
              <TextField
                fullWidth
                label={t('employee.verificationList.filters.documentNumber')}
                value={filters.documentNumber}
                onChange={(e) => handleFilterChange('documentNumber', e.target.value)}
                size="small"
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6, md: 3 }}>
              <FormControl fullWidth size="small">
                <InputLabel>{t('employee.verificationList.filters.documentType')}</InputLabel>
                <Select
                  value={filters.documentType}
                  label={t('employee.verificationList.filters.documentType')}
                  onChange={(e) => handleFilterChange('documentType', e.target.value as string)}
                >
                  <MenuItem value=""><em>{t('employee.verificationList.filters.clear')}</em></MenuItem>
                  <MenuItem value="PESEL">PESEL</MenuItem>
                  <MenuItem value="PASSPORT">{t('employee.createVerification.passport')}</MenuItem>
                  <MenuItem value="NIP">NIP</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid size={{ xs: 12, sm: 6, md: 3 }}>
              <FormControl fullWidth size="small">
                <InputLabel>{t('employee.verificationList.filters.status')}</InputLabel>
                <Select
                  value={filters.status}
                  label={t('employee.verificationList.filters.status')}
                  onChange={(e) => handleFilterChange('status', e.target.value as string)}
                >
                  <MenuItem value=""><em>{t('employee.verificationList.filters.clear')}</em></MenuItem>
                  <MenuItem value="IN_PROGRESS">{t('employee.verificationList.statuses.IN_PROGRESS')}</MenuItem>
                  <MenuItem value="AWAITING_FINAL_DECISION">{t('employee.verificationList.statuses.AWAITING_FINAL_DECISION')}</MenuItem>
                  <MenuItem value="VERIFIED">{t('employee.verificationList.statuses.VERIFIED')}</MenuItem>
                  <MenuItem value="FAILED">{t('employee.verificationList.statuses.FAILED')}</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid size={{ xs: 12, sm: 6, md: 3 }}>
              <TextField
                fullWidth
                label={t('employee.verificationList.filters.email')}
                value={filters.email}
                onChange={(e) => handleFilterChange('email', e.target.value)}
                size="small"
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6, md: 3 }}>
              <TextField
                fullWidth
                type="date"
                label={t('employee.verificationList.filters.startDateFrom')}
                value={filters.startDateFrom}
                onChange={(e) => handleFilterChange('startDateFrom', e.target.value)}
                size="small"
                slotProps={{ inputLabel: { shrink: true } }}
              />
            </Grid>
            <Grid size={{ xs: 12, sm: 6, md: 3 }}>
              <TextField
                fullWidth
                type="date"
                label={t('employee.verificationList.filters.startDateTo')}
                value={filters.startDateTo}
                onChange={(e) => handleFilterChange('startDateTo', e.target.value)}
                size="small"
                slotProps={{ inputLabel: { shrink: true } }}
              />
            </Grid>
            <Grid size={{ xs: 12 }} sx={{ display: 'flex', justifyContent: 'flex-end' }}>
              <Button 
                startIcon={<ClearIcon />} 
                onClick={clearFilters}
                color="inherit"
              >
                {t('employee.verificationList.filters.clear')}
              </Button>
            </Grid>
          </Grid>
        </Paper>
      </Collapse>

      {isError && (
        <Alert severity="error" sx={{ mb: 2, width: '100%' }}>
          {t('client.personalInfo.error', { message: error instanceof Error ? error.message : 'Unknown error' })}
        </Alert>
      )}

      <TableContainer component={Paper} sx={{ width: '100%', overflowX: 'auto' }}>
        <Table sx={{ minWidth: 1000, width: '100%' }}>
          <TableHead>
            <TableRow>
              <TableCell sortDirection={sortBy === 'VERIFICATION_STATUS' ? sortOrder.toLowerCase() as any : false}>
                <TableSortLabel
                  active={sortBy === 'VERIFICATION_STATUS'}
                  direction={sortBy === 'VERIFICATION_STATUS' ? sortOrder.toLowerCase() as any : 'asc'}
                  onClick={() => handleSort('VERIFICATION_STATUS')}
                >
                  {t('employee.verificationList.status')}
                </TableSortLabel>
              </TableCell>
              <TableCell>
                <TableSortLabel
                  active={sortBy === 'FIRST_NAME' || sortBy === 'LAST_NAME'}
                  direction={sortBy === 'FIRST_NAME' || sortBy === 'LAST_NAME' ? sortOrder.toLowerCase() as any : 'asc'}
                  onClick={() => handleSort('LAST_NAME')}
                >
                  {t('employee.verificationList.client')}
                </TableSortLabel>
              </TableCell>
              <TableCell>
                <TableSortLabel
                  active={sortBy === 'IDENTITY_DOCUMENT_NUMBER'}
                  direction={sortBy === 'IDENTITY_DOCUMENT_NUMBER' ? sortOrder.toLowerCase() as any : 'asc'}
                  onClick={() => handleSort('IDENTITY_DOCUMENT_NUMBER')}
                >
                  {t('employee.verificationList.document')}
                </TableSortLabel>
              </TableCell>
              <TableCell>
                <TableSortLabel
                  active={sortBy === 'START_DATE'}
                  direction={sortBy === 'START_DATE' ? sortOrder.toLowerCase() as any : 'asc'}
                  onClick={() => handleSort('START_DATE')}
                >
                  {t('employee.verificationList.startDate')}
                </TableSortLabel>
              </TableCell>
              <TableCell>
                <TableSortLabel
                  active={sortBy === 'FINISH_DATE'}
                  direction={sortBy === 'FINISH_DATE' ? sortOrder.toLowerCase() as any : 'asc'}
                  onClick={() => handleSort('FINISH_DATE')}
                >
                  {t('employee.verificationList.finishDate')}
                </TableSortLabel>
              </TableCell>
              <TableCell align="right">{t('employee.verificationList.actions')}</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {isLoading ? (
              <TableRow>
                <TableCell colSpan={6} align="center" sx={{ py: 3 }}>
                  <CircularProgress size={24} />
                </TableCell>
              </TableRow>
            ) : data?.rows.length === 0 ? (
              <TableRow>
                <TableCell colSpan={6} align="center" sx={{ py: 3 }}>
                  {t('employee.verificationList.noData')}
                </TableCell>
              </TableRow>
            ) : (
              data?.rows.map((verification) => (
                <TableRow key={verification.id}>
                  <TableCell>
                    <Chip 
                      icon={getStatusIcon(verification.status)}
                      label={t(`employee.verificationList.statuses.${verification.status}`)} 
                      color={getStatusColor(verification.status)}
                      variant="outlined"
                      sx={{ fontWeight: 'bold' }}
                    />
                  </TableCell>
                  <TableCell>
                    {verification.client 
                      ? `${verification.client.firstName} ${verification.client.lastName}` 
                      : '-'}
                  </TableCell>
                  <TableCell>
                    {`${verification.identityDocument.type}: ${verification.identityDocument.number}`}
                  </TableCell>
                  <TableCell>{formatDate(verification.startDate)}</TableCell>
                  <TableCell>{formatDate(verification.finishDate)}</TableCell>
                  <TableCell align="right">
                    <IconButton 
                      size="small" 
                      onClick={() => navigate(`/employee/verification/${verification.id}`)}
                    >
                      <VisibilityIcon fontSize="small" />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
        <TablePagination
          rowsPerPageOptions={[5, 10, 25]}
          component="div"
          count={data?.totalElements || 0}
          rowsPerPage={pageSize}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
          labelRowsPerPage={t('employee.verificationList.rowsPerPage')}
        />
      </TableContainer>
    </Box>
  );
};

export default VerificationList;
