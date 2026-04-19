import React, { FC } from 'react';
import { Box, Paper, Typography, Stack, List, ListItem, ListItemText, Divider, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import { useTranslation } from 'react-i18next';

interface ReportDataViewProps {
  data: any;
}

const ReportDataView: FC<ReportDataViewProps> = ({ data }) => {
  const { t } = useTranslation();
  
  let report: any;
  try {
    report = typeof data === 'string' ? JSON.parse(data) : data;
  } catch (e) {
    return <Typography color="error">Error parsing report data</Typography>;
  }

  const accountInfo = report?.accountInfo;
  const owners = accountInfo?.owners || [];
  const accountsData = accountInfo?.accounts;
  const accounts = Array.isArray(accountsData) ? accountsData : (accountsData ? [accountsData] : []);

  return (
    <Stack spacing={3}>
      {owners.length > 0 && (
        <Box>
          <Typography variant="h6" gutterBottom>
            {t('employee.verificationDetails.bank.owners')}
          </Typography>
          <Paper variant="outlined">
            <List dense>
              {owners.map((owner: any, index: number) => (
                <React.Fragment key={index}>
                  <ListItem>
                    <ListItemText 
                      primary={owner.name} 
                      secondary={owner.kind && owner.address ? `${owner.kind} - ${owner.address}` : (owner.kind || owner.address)} 
                    />
                  </ListItem>
                  {index < owners.length - 1 && <Divider />}
                </React.Fragment>
              ))}
            </List>
          </Paper>
        </Box>
      )}

      {accounts.length > 0 && (
        <Box>
          <Typography variant="h6" gutterBottom>
            {t('employee.verificationDetails.bank.accounts')}
          </Typography>
          <TableContainer component={Paper} variant="outlined">
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell>IBAN</TableCell>
                  <TableCell>Name</TableCell>
                  <TableCell>Currency</TableCell>
                  <TableCell align="right">Balance</TableCell>
                  <TableCell>Active Since</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {accounts.map((account: any, index: number) => (
                  <TableRow key={index}>
                    <TableCell>{account.iban}</TableCell>
                    <TableCell>{account.name}</TableCell>
                    <TableCell>{account.currencyName}</TableCell>
                    <TableCell align="right">{account.currencyBalance}</TableCell>
                    <TableCell>{account.activeSinceAtLeast}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </Box>
      )}
    </Stack>
  );
};

export default ReportDataView;
