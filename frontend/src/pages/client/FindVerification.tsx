import React, { FC, useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
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
  Paper
} from '@mui/material';
import { useMutation } from "@tanstack/react-query";
import { getVerificationByDocument } from '../../api/verification';
import { DocumentType } from '../../types/api';
import { isValidPesel, isValidNip } from '../../utils/validation';

const FindVerification: FC = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [docType, setDocType] = useState<DocumentType>('PESEL');
  const [docNumber, setDocNumber] = useState('');
  const [docNumberError, setDocNumberError] = useState<string | null>(null);

  const mutation = useMutation({
    mutationFn: () => getVerificationByDocument(docType, docNumber),
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!docNumber.trim()) {
      setDocNumberError(t('client.checkVerification.docNumberRequired'));
      return;
    }

    if (docType === 'PESEL' && !isValidPesel(docNumber)) {
      setDocNumberError(t('client.checkVerification.invalidPesel'));
      return;
    }

    if (docType === 'NIP' && !isValidNip(docNumber)) {
      setDocNumberError(t('client.checkVerification.invalidNip'));
      return;
    }

    mutation.mutate();
  };

  useEffect(() => {
    if (mutation.isSuccess && mutation.data) {
      navigate(`/client/verification/${mutation.data.id}`, { state: { fromFindVerification: true } });
    }
  }, [mutation.isSuccess, mutation.data, navigate]);

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Paper elevation={3} sx={{ p: 4, width: '100%', mt: 2 }}>
          <Typography variant="h6" gutterBottom>
            {t('client.checkVerification.title')}
          </Typography>

          <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 3, mt: 2 }}>
            <FormControl fullWidth>
              <InputLabel id="doc-type-label">{t('client.checkVerification.documentType')}</InputLabel>
              <Select
                labelId="doc-type-label"
                value={docType}
                label={t('client.checkVerification.documentType')}
                onChange={(e) => {
                  setDocType(e.target.value as DocumentType);
                  mutation.reset();
                }}
              >
                <MenuItem value="PESEL">{t('client.checkVerification.pesel')}</MenuItem>
                <MenuItem value="PASSPORT">{t('client.checkVerification.passport')}</MenuItem>
                <MenuItem value="NIP">{t('client.checkVerification.nip')}</MenuItem>
              </Select>
            </FormControl>

            <TextField
              fullWidth
              label={t('client.checkVerification.documentNumber')}
              value={docNumber}
              onChange={(e) => {
                setDocNumber(e.target.value);
                setDocNumberError(null);
                mutation.reset();
              }}
              error={!!docNumberError}
              helperText={docNumberError || ''}
              required
            />

            <Button
              type="submit"
              variant="contained"
              size="large"
              disabled={mutation.isPending}
              startIcon={mutation.isPending ? <CircularProgress size={20} /> : null}
            >
              {t('client.checkVerification.submit')}
            </Button>

            {mutation.isSuccess && !mutation.data && (
              <Alert severity="info">
                {t('client.checkVerification.notExists')}
              </Alert>
            )}

            {mutation.isError && (
              <Alert severity="error">
                {mutation.error.message}
              </Alert>
            )}
          </Box>
        </Paper>
      </Box>
    </Container>
  );
};

export default FindVerification;
