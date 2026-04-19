import React, { FC, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { 
  Typography, 
  Container, 
  Box, 
  TextField, 
  Button, 
  FormControl, 
  InputLabel, 
  Select, 
  MenuItem, 
  Alert, 
  CircularProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  IconButton,
  Backdrop
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { useMutation, useQueryClient } from "@tanstack/react-query"
import { createVerification } from '../../api/verification';
import { DocumentType } from '../../types/api';
import { isValidPesel, isValidNip } from '../../utils/validation';
import VerificationList from './VerificationList';

const EmployeePage: FC = () => {
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const [open, setOpen] = useState(false);
  const [docType, setDocType] = useState<DocumentType>('PESEL');
  const [docNumber, setDocNumber] = useState('');
  const [docNumberError, setDocNumberError] = useState<string | null>(null);

  const mutation = useMutation({
    mutationFn: createVerification,
    onSuccess: () => {
      setDocType('PESEL');
      setDocNumber('');
      setDocNumberError(null);
      // Refresh the verification list after successful creation
      queryClient.invalidateQueries({ queryKey: ['verifications'] });
    }
  });

  const handleOpen = () => setOpen(true);
  const handleClose = () => {
    setOpen(false);
    setDocNumberError(null);
    mutation.reset();
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!docNumber.trim()) {
      setDocNumberError(t('employee.createVerification.docNumberRequired'));
      return;
    }

    if (docType === 'PESEL' && !isValidPesel(docNumber)) {
      setDocNumberError(t('employee.createVerification.invalidPesel'));
      return;
    }

    if (docType === 'NIP' && !isValidNip(docNumber)) {
      setDocNumberError(t('employee.createVerification.invalidNip'));
      return;
    }

    mutation.mutate({ type: docType, number: docNumber });
  };

  return (
    <Container maxWidth={false} sx={{ maxWidth: '1440px' }}>
      <Box sx={{ 
        mt: 4, 
        display: 'flex', 
        flexDirection: 'column', 
        alignItems: 'center',
        gap: 3 
      }}>
        <Button 
          variant="contained" 
          size="large" 
          onClick={handleOpen}
          sx={{ py: 1.5, px: 4 }}
        >
          {t('employee.createVerification.title')}
        </Button>

        <Dialog 
          open={open} 
          onClose={handleClose}
          fullWidth
          maxWidth="sm"
          slots={{ backdrop: Backdrop }}
          slotProps={{
            backdrop: {
              sx: {
                backdropFilter: 'blur(4px)',
                backgroundColor: 'rgba(0, 0, 0, 0.2)',
              },
            },
          }}
        >
          <DialogTitle sx={{ m: 0, p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Typography variant="h6">
              {t('employee.createVerification.title')}
            </Typography>
            <IconButton
              aria-label="close"
              onClick={handleClose}
              sx={{ color: (theme) => theme.palette.grey[500] }}
            >
              <CloseIcon />
            </IconButton>
          </DialogTitle>
          
          <Box component="form" onSubmit={handleSubmit}>
            <DialogContent dividers>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, py: 1 }}>
                <FormControl fullWidth>
                  <InputLabel id="doc-type-label">{t('employee.createVerification.documentType')}</InputLabel>
                  <Select
                    labelId="doc-type-label"
                    value={docType}
                    label={t('employee.createVerification.documentType')}
                    onChange={(e) => setDocType(e.target.value as DocumentType)}
                  >
                    <MenuItem value="PESEL">{t('employee.createVerification.pesel')}</MenuItem>
                    <MenuItem value="PASSPORT">{t('employee.createVerification.passport')}</MenuItem>
                    <MenuItem value="NIP">{t('employee.createVerification.nip')}</MenuItem>
                  </Select>
                </FormControl>

                <TextField
                  fullWidth
                  label={t('employee.createVerification.documentNumber')}
                  value={docNumber}
                  onChange={(e) => {
                    setDocNumber(e.target.value);
                    if (e.target.value.trim()) {
                      setDocNumberError(null);
                    }
                  }}
                  error={!!docNumberError}
                  helperText={docNumberError || ''}
                  required
                />

                {mutation.isSuccess && (
                  <Alert severity="success">
                    {t('employee.createVerification.success', { id: mutation.data.verificationId })}
                  </Alert>
                )}

                {mutation.isError && (
                  <Alert severity="error">
                    {t('employee.createVerification.error', { message: mutation.error.message })}
                  </Alert>
                )}
              </Box>
            </DialogContent>
            
            <DialogActions sx={{ p: 2 }}>
              <Button onClick={handleClose} color="inherit">
                Anuluj
              </Button>
              <Button
                type="submit"
                variant="contained"
                disabled={mutation.isPending}
                startIcon={mutation.isPending ? <CircularProgress size={20} /> : null}
              >
                {t('employee.createVerification.submit')}
              </Button>
            </DialogActions>
          </Box>
        </Dialog>
        
        <VerificationList />
      </Box>
    </Container>
  );
};

export default EmployeePage;
